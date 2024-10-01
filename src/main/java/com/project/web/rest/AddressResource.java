package com.project.web.rest;

import com.project.service.DistrictService;
import com.project.service.ProvinceService;
import com.project.service.WardService;
import com.project.service.dto.DistrictDTO;
import com.project.service.dto.ProvinceDTO;
import com.project.service.dto.WardDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class AddressResource {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private WardService wardService;

    @GetMapping("/locations")
    public ResponseEntity<Map<String, Object>> getALlLocations() {
        Map<String, Object> response = new HashMap<>();
        List<ProvinceDTO> provinces = provinceService.findAllAsDto();
        response.put("provinces", provinces);

        List<DistrictDTO> districts = districtService.findAllDTO();
        response.put("districts", districts);

        List<WardDTO> wards = wardService.findAllDTO();
        response.put("wards", wards);

        return ResponseEntity.ok(response);
    }
}
