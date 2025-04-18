package com.ravuri.calibration.service.impl;

import com.ravuri.calibration.entity.Office;
import com.ravuri.calibration.entity.OfficeType;
import com.ravuri.calibration.exception.OfficeNotFoundException;
import com.ravuri.calibration.exception.ValidationException;
import com.ravuri.calibration.repository.OfficeRepository;
import com.ravuri.calibration.service.OfficeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

@Service
public class OfficeServiceImpl implements OfficeService {

    private static final Logger LOGGER = LogManager.getLogger(OfficeServiceImpl.class);;
    @Autowired
    private OfficeRepository officeRepository;

    private static final Map<String, String> COUNTRY_PINCODE_PATTERNS = new HashMap<>();


    static {
        // India: 6 digits
        COUNTRY_PINCODE_PATTERNS.put("India", "^[1-9][0-9]{5}$");
        // USA: 5 digits or 5+4 digits
        COUNTRY_PINCODE_PATTERNS.put("USA", "^\\d{5}(-\\d{4})?$");
        // UK: Complex alphanumeric pattern
        COUNTRY_PINCODE_PATTERNS.put("UK", "^[A-Z]{1,2}[0-9][A-Z0-9]? ?[0-9][A-Z]{2}$");
        // Canada: Letter Number Letter Number Letter Number
        COUNTRY_PINCODE_PATTERNS.put("Canada", "^[A-Z]\\d[A-Z] ?\\d[A-Z]\\d$");
        // Australia: 4 digits
        COUNTRY_PINCODE_PATTERNS.put("Australia", "^\\d{4}$");
        // Germany: 5 digits
        COUNTRY_PINCODE_PATTERNS.put("Germany", "^\\d{5}$");
        // France: 5 digits
        COUNTRY_PINCODE_PATTERNS.put("France", "^\\d{5}$");
        // Japan: 7 digits with optional hyphen
        COUNTRY_PINCODE_PATTERNS.put("Japan", "^\\d{3}-?\\d{4}$");
    }


    @Override
    @Transactional(readOnly = true)
    public List<Office> getAllOffices() {
        LOGGER.info("Fetching all offices");
        return officeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Office getOfficeById(Long id) {
        LOGGER.info("Fetching office with ID: {}", id);
        return officeRepository.findById(id)
                .orElseThrow(() -> new OfficeNotFoundException("Office not found with id: " + id));
    }

    @Override
    @Transactional
    public Office createOffice(Office office) {
        LOGGER.info("Creating new office: {}", office);
        validateOffice(office);
        return officeRepository.save(office);
    }

    @Override
    @Transactional
    public Office updateOffice(Long id, Office officeDetails) {
        LOGGER.info("Updating office with ID: {} with details: {}", id, officeDetails);
        Office office = getOfficeById(id);

        validateOffice(officeDetails);

        office.setName(officeDetails.getName());
        office.setAddress(officeDetails.getAddress());
        office.setCity(officeDetails.getCity());
        office.setState(officeDetails.getState());
        office.setCountry(officeDetails.getCountry());
        office.setPincode(officeDetails.getPincode());
        office.setPhone(officeDetails.getPhone());
        office.setEmail(officeDetails.getEmail());
        office.setType(officeDetails.getType());
        office.setDescription(officeDetails.getDescription());

        return officeRepository.save(office);
    }

    @Override
    @Transactional
    public void deleteOffice(Long id) {
        LOGGER.info("Deleting office with ID: {}", id);
        if (!officeRepository.existsById(id)) {
            throw new OfficeNotFoundException("Office not found with id: " + id);
        }
        officeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Office> getOfficesByType(OfficeType type) {
        LOGGER.info("Fetching offices with type: {}", type);
        return officeRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Office> getOfficesByCountry(String country) {
        LOGGER.info("Fetching offices in country: {}", country);

        if (country == null || country.trim().isEmpty()) {
            throw new ValidationException("Country cannot be empty");
        }
        return officeRepository.findByCountry(country);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Office> getOfficesByState(String state) {
        LOGGER.info("Fetching offices in state: {}", state);
        if (state == null || state.trim().isEmpty()) {
            throw new ValidationException("State cannot be empty");
        }
        return officeRepository.findByState(state);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Office> getOfficesByCity(String city) {
        LOGGER.info("Fetching offices in city: {}", city);
        if (city == null || city.trim().isEmpty()) {
            throw new ValidationException("City cannot be empty");
        }
        return officeRepository.findByCity(city);
    }

    private void validateOffice(Office office) {
        if (office.getType() == null) {
            throw new ValidationException("Office type is required");
        }

        if (office.getPincode() != null && !office.getPincode().matches("^\\d{6}$")) {
            throw new ValidationException("Invalid pincode format. Must be 6 digits");
        }

        validatePincode(office.getCountry(), office.getPincode());
    }

    private void validatePincode(String country, String pincode) {
        if (pincode == null || pincode.trim().isEmpty()) {
            throw new ValidationException("Pincode cannot be empty");
        }

        String pattern = COUNTRY_PINCODE_PATTERNS.get(country);
        if (pattern != null) {
            if (!Pattern.matches(pattern, pincode)) {
                throw new ValidationException(String.format("Invalid pincode format for %s", country));
            }
        } else {
            // For countries not in the map, use a generic validation
            if (!pincode.matches("^[A-Z0-9-\\s]{3,10}$")) {
                throw new ValidationException("Invalid pincode format");
            }
        }
    }

}
