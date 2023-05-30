package com.springboot.service;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.springboot.model.Vehicle;
import com.springboot.repository.VehicleRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VehicleService {
	private static final Exception VehicleAllReadyExist = null;
	@Autowired
	private VehicleRepository vehicleRepository;

	public Vehicle GetVehicleById(String id) {
		try {
			Vehicle vehicle = vehicleRepository.findById(id).get();
//			vehicle.setRegistrationFrontDocument(id);
			return vehicle;
		} catch (Exception e) {
			return null;
		}
	}

	public List<Vehicle> GetAllVehicle() {
		List<Vehicle> vehicle = new ArrayList<>();
		vehicleRepository.findAll().forEach(vehicle::add);
//		vehicleRepository.findByCustomerId(customerid).forEach(vehicle::add);
		return vehicle;
	}

	public Vehicle AddVehicle(Vehicle vehicle) {
		try {
			vehicleRepository.save(vehicle);
		} catch (Exception e) {
			System.out.println(e);

		}
		return vehicle;

	}

	public Vehicle UpdateVehicle(Vehicle vehicle, int vehicleId) {
		vehicle.setId(vehicleId);
		vehicle.setRegistrationNumber(vehicle.getRegistrationNumber());
		vehicleRepository.save(vehicle);
		return vehicle;
	}

	public void VehicleSoftDeleted(int vehicleid) {
		vehicleRepository.deleteById(Integer.toString(vehicleid));
	}
}

//	public void SaveData(int vehicleId, Map map, HttpServletRequest request) {
//		String sql = "update vehicle set ";
//
//		Set<String> keys = map.keySet();
//		for (String key : keys) {
//			sql += key + " = ?,";
//		}
//		sql = sql.substring(0, sql.length() - 1);
//		sql += " where id = ?";
//
//		try {
//			DAOConnection db = new DAOConnection();
//			db.openConnection();
//			PreparedStatement pr = db.con.prepareStatement(sql);
//			int i = 1;
//			for (String key : keys) {
//				pr.setString(i, map.get(key).toString());
//				i++;
//			}
//			pr.setInt(i, vehicleId);
//			pr.executeUpdate();
//			db.closeConnection();
//			AuditTracker.AddAuditForVehicle(vehicleId, request);
//		} catch (Exception ex) {
//			System.err.println("Error in vehicle savedata");
//			ex.printStackTrace();
//		}
//
//	}


