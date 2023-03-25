package com.example.milemate.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DBManager(application: Application) : AndroidViewModel(application) {
    private var database : CarDatabase;

    init{
        database = CarDatabase.getDatabase(application);
    }

    fun insertCar(car : com.example.milemate.Car){
        viewModelScope.launch(Dispatchers.IO) {
            val carToInsert = Car(0, car.carName, car.carBrand, car.carMileage.toInt());
            database.carDao().insertCar(carToInsert);
        }
    }

    //Checks if data (cars) is present in database
    fun hasData(): Boolean {
        val db = this.database
        val countQuery = "SELECT count(*) FROM $CarDatabase"
        val cursor = db.query(countQuery, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }
}