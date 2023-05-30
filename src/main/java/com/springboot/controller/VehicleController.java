package com.springboot.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.springboot.converter.VehicleConverter;
import com.springboot.model.Vehicle;
import com.springboot.service.VehicleService;
import com.springboot.tools.JSONTools;
import com.springboot.tools.MiscMethods;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class VehicleController {

	@Autowired
	private VehicleService vehicleService;

	@CrossOrigin(origins = "*")
	@GetMapping(value = { "/vehicle", "/vehicle/{vehicleid}" })
	@ResponseBody
	public ResponseEntity getVehicles(@PathVariable(required = false) Optional<String> vehicleid,
			HttpServletRequest request) {
		String output = "";
//		User login_user = MiscMethods.GetUserBasedOnToken(request.getHeader("USER_API_TOKEN"));
//		int customerId = login_user.getCustomerId();
//		if (login_user.getId() <= 0) {
//			log.debug("loginR:"+login_user.getId());
//			return new ResponseEntity("User not logged in", HttpStatus.FORBIDDEN);
//		}
		List<Vehicle> vehicle_list = new ArrayList();
		if (vehicleid.isPresent()) {
			String new_vehiId = vehicleid.get();
			if (new_vehiId.contains(",")) {
				if (new_vehiId.endsWith(",")) {
					new_vehiId = new_vehiId.substring(0, new_vehiId.length() - 1);
				}
				for (String vehID_row : new_vehiId.split(",")) {
					vehicle_list.add(vehicleService.GetVehicleById(vehID_row));
				}
			} else {
				Vehicle vehicle = new Vehicle();
				vehicle = vehicleService.GetVehicleById(new_vehiId);
				vehicle_list.add(vehicle);
			}
			if (new_vehiId.length() < 1) {
				return new ResponseEntity("invalid request", HttpStatus.BAD_REQUEST);
			}
		} else {
			vehicle_list = vehicleService.GetAllVehicle();
		}

		JsonNode vehicle_nodes = null;
		ObjectMapper json_obj_mapper = new ObjectMapper();
		ArrayNode vehicle_array = json_obj_mapper.createArrayNode();
		if (!vehicle_list.isEmpty()) {
			Vehicle vehicle_temp = null;
			try {
				for (int i = 0; i < vehicle_list.size(); i++) {

					try {
						vehicle_temp = vehicle_list.get(i);
						if (vehicle_temp != null) {
							vehicle_nodes = JSONTools
									.GetJSONFromString(VehicleConverter.toJsonString(vehicle_list.get(i)));
							vehicle_array.add(vehicle_nodes);

						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				output = json_obj_mapper.writeValueAsString(vehicle_array);
			} catch (Exception ex) {
				return new ResponseEntity("Error in getVehicle", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			output = "[]";
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@CrossOrigin(origins = "*")
	@PostMapping(value = "/vehicle", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity addVehicle(HttpServletRequest request) {
		String response = "";
//		User login_user = MiscMethods.GetUserBasedOnToken(request.getHeader("USER_API_TOKEN"));
//
//		if (login_user.getId() <= 0) {
//			return new ResponseEntity("User not logged in", HttpStatus.FORBIDDEN);
//		}

		String VehicleDetails = MiscMethods.GetHTTPRequestBody(request);

		try {
			Vehicle ve = VehicleConverter.fromJsonString(VehicleDetails);

			response = VehicleConverter.toJsonString(ve);
			if (ve.getId() < 0) {
				return new ResponseEntity("Unable to add vehicle", HttpStatus.BAD_REQUEST);
			} else if (ve.getId() == 0) {
				return new ResponseEntity("already exists", HttpStatus.CONFLICT);
			} else {
				return new ResponseEntity(ve, HttpStatus.OK);
			}

		} catch (Exception ex) {
			System.out.println("Error in addVehicle " + ex);
			return new ResponseEntity("Error in addVehicle", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//
	@CrossOrigin(origins = "*")
	@PutMapping(value = "/vehicle/{vehicleId}")
	@ResponseBody
	public ResponseEntity updateVehicle(@PathVariable(required = true) String vehicleId, HttpServletRequest request) {
		String response = "";
//		User login_user = MiscMethods.GetUserBasedOnToken(request.getHeader("USER_API_TOKEN"));
//
//		if (login_user.getId() <= 0) {
//			return new ResponseEntity("User not logged in", HttpStatus.FORBIDDEN);
//		}
		String vehicleDetails = MiscMethods.GetHTTPRequestBody(request);

		if (vehicleId == null || vehicleDetails == null) {
			return new ResponseEntity("Required ID and POST DATA. URL format: /vehicle/{vehicleId}",
					HttpStatus.BAD_REQUEST);
		} else {
			try {
				Vehicle ve = VehicleConverter.fromJsonString(vehicleDetails);

				if (Integer.parseInt(vehicleId) != ve.getId()) {
					return new ResponseEntity("Unable to update Vehicle. Data does not match request",
							HttpStatus.BAD_REQUEST);
				}
				ve = vehicleService.UpdateVehicle(ve, Integer.parseInt(vehicleId));

				ve = VehicleConverter.fromJsonString(vehicleDetails);

				response = VehicleConverter.toJsonString(ve);

				if (ve.getId() > 0) {
					return new ResponseEntity(response, HttpStatus.OK);
				} else {
					return new ResponseEntity("Unable to update Vehicle", HttpStatus.BAD_REQUEST);
				}

			} catch (Exception ex) {
				System.out.println("Error in updateVehicle" + ex);
				return new ResponseEntity("Error in updateVehicle", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	}

//
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = "/vehicle/{vehicleId}")
	@ResponseBody
	public ResponseEntity deleteVehicle(@PathVariable(required = true) String vehicleId, HttpServletRequest request) {

		if (vehicleId == null) {
			return new ResponseEntity("Required ID and POST DATA in JSON format. URL format: /vehicle/{vehicleId}",
					HttpStatus.BAD_REQUEST);
		}
		try {

			vehicleService.VehicleSoftDeleted(Integer.parseInt(vehicleId));
			return new ResponseEntity("", HttpStatus.NO_CONTENT); // using security

		} catch (Exception ex) {
			System.out.println("Error in DeleteVehicle" + ex);
			return new ResponseEntity("Error in DeleteVehicle", HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
//
//	@CrossOrigin(origins = "*")
//	@PatchMapping(value = "/vehicle/{vehicleid}", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity patchVehicle(@PathVariable int vehicleid, HttpServletRequest request) {
//
//		String emp_json = MiscMethods.GetHTTPRequestBody(request);
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			Map<String, Object> map = mapper.readValue(emp_json, new TypeReference<Map<String, Object>>() {
//			});
//			System.out.println(map);
//
//			Set<String> keys = map.keySet();
//			Collection vals = map.values();
//			log.debug("vals:::" + vals);
//
//			vehicleService.SaveData(vehicleid, map, request);
//			log.debug(emp_json);
//
//			return new ResponseEntity("{\"status\":\"success\"}", HttpStatus.OK);
//		} catch (Exception ex) {
//			log.debug(ex);
//			return new ResponseEntity("{\"status\":\"failed\",\"message\":\"" + ex.getMessage() + "\"}", HttpStatus.OK);
//		}
//	}
}