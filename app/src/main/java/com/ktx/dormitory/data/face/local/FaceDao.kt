package com.ktx.dormitory.data.face.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFace(face: FaceEntity)

    @Query("SELECT * FROM registered_faces WHERE studentId = :studentId")
    suspend fun getFaceByStudentId(studentId: String): FaceEntity?

    @Query("SELECT * FROM registered_faces")
    suspend fun getAllFaces(): List<FaceEntity>

    @Query("DELETE FROM registered_faces WHERE studentId = :studentId")
    suspend fun deleteFace(studentId: String)
}
