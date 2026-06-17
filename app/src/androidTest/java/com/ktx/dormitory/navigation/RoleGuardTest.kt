package com.ktx.dormitory.navigation

import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.ktx.dormitory.domain.model.UserData
import com.ktx.dormitory.presentation.features.auth.LoginViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoleGuardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Khai báo ViewModel là Mock
    private val loginViewModel = mockk<LoginViewModel>(relaxed = true)
    private val userDataFlow = MutableStateFlow<UserData?>(null)

    private val secretContent = "SENSITIVE_DATA_INTERNAL"
    private val deniedMessage = "Bạn không có quyền truy cập chức năng này"
    private val loadingMessage = "Đang xác thực quyền..."

    @Before
    fun setup() {
        // Mock luồng dữ liệu User
        // Bây giờ sẽ hoạt động vì loginViewModel.userData đã là 'open'
        every { loginViewModel.userData } returns userDataFlow
    }

    @Test
    fun access_granted_for_valid_user_role() {
        userDataFlow.value = UserData("sv01", "USER", "Nguyen Van A")
        setupRoleGuard(listOf("USER"))
        composeTestRule.onNodeWithText(secretContent).assertExists()
    }

    @Test
    fun access_granted_for_valid_staff_role() {
        userDataFlow.value = UserData("staff01", "STAFF", "Can Bo A")
        setupRoleGuard(listOf("STAFF"))
        composeTestRule.onNodeWithText(secretContent).assertExists()
    }

    @Test
    fun access_granted_for_valid_admin_role() {
        userDataFlow.value = UserData("admin01", "ADMIN", "Quan Tri Vien")
        setupRoleGuard(listOf("ADMIN"))
        composeTestRule.onNodeWithText(secretContent).assertExists()
    }

    @Test
    fun access_denied_for_unauthorized_role() {
        userDataFlow.value = UserData("sv01", "USER", "Nguyen Van A")
        setupRoleGuard(listOf("STAFF"))
        composeTestRule.onNodeWithText(deniedMessage).assertExists()
        composeTestRule.onNodeWithText(secretContent).assertDoesNotExist()
    }

    @Test
    fun access_allowed_for_lowercase_role_string() {
        userDataFlow.value = UserData("sv01", "user", "Nguyen Van A")
        setupRoleGuard(listOf("USER"))
        composeTestRule.onNodeWithText(secretContent).assertExists()
    }

    @Test
    fun shows_loading_indicator_when_userdata_is_null() {
        userDataFlow.value = null
        setupRoleGuard(listOf("USER"))
        composeTestRule.onNodeWithText(loadingMessage).assertExists()
        composeTestRule.onNodeWithText(secretContent).assertDoesNotExist()
    }

    private fun setupRoleGuard(requiredRoles: List<String>) {
        composeTestRule.setContent {
            RoleGuard(
                requiredRoles = requiredRoles,
                loginViewModel = loginViewModel
            ) {
                Text(secretContent)
            }
        }
    }
}
