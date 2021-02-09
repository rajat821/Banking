package com.care.banking.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.care.banking.ThreadExecutor

@Database(entities = arrayOf(Users::class, Transaction::class), version = 3, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun trasactionDao(): TransactionDao

    companion object {

        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "word_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

                INSTANCE = instance

                instance
            }

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(INSTANCE!!)
            }
        }

        private fun populateDatabase(db: UserDatabase) {
            val userDao = db.userDao()
            ThreadExecutor.getInstance()?.diskIO?.execute(Runnable {
                kotlin.run {
                    userDao.insert(
                        Users(
                            "Rahul Kumar",
                            20000.00f,
                            "SPRK12345",
                            1452036541,
                            "6523012457",
                            "rahulkumar@gmail.com",
                            "Ranchi"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Rajat Kumar",
                            50000.00f,
                            "SPRK12345",
                            1452036542,
                            "9865321456",
                            "rajatkumar@gmail.com",
                            "Patna"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Suraj Kumar",
                            45000.00f,
                            "SPRK12345",
                            1452036543,
                            "8563214569",
                            "surajkumar@gmail.com",
                            "Delhi"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Kumaresh",
                            58000.00f,
                            "SPRK12345",
                            1452036544,
                            "7854123659",
                            "kumaresh@gmail.com",
                            "Kolkata"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Shubham Kumar",
                            42000.00f,
                            "SPRK12345",
                            1452036545,
                            "9985632002",
                            "shubham@gmail.com",
                            "Mumbai"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Shibu Singh",
                            89000.00f,
                            "SPRK12345",
                            1452036546,
                            "6200325698",
                            "shibu@gmail.com",
                            "Bangalore"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Aniket Singh",
                            65000.00f,
                            "SPRK12345",
                            1452036547,
                            "7788956321",
                            "aniket@gmail.com",
                            "Hyderabad"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Aakash",
                            63000.00f,
                            "SPRK12345",
                            1452036548,
                            "9865477785",
                            "aakash@gmail.com",
                            "Chennai"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Tishan",
                            40000.00f,
                            "SPRK12345",
                            1452036549,
                            "8855663320",
                            "tishan@gmail.com",
                            "Bhopal"
                        )
                    )
                    userDao.insert(
                        Users(
                            "Amit Kumar",
                            56000.00f,
                            "SPRK12345",
                            1452036550,
                            "7756984123",
                            "amit@gmail.com",
                            "Lucknow"
                        )
                    )
                }
            })
        }
    }
}