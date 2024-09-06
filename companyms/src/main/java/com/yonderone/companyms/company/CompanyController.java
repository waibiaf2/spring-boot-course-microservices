package com.yonderone.companyms.company;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return new ResponseEntity<>(
            companyService.findAll(),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        if (company != null) {
            return new ResponseEntity<>(
                companyService.getCompanyById(id),
                HttpStatus.OK
            );
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<String> addCompany(@RequestBody Company company) {
        companyService.createCompany(company);
        return new ResponseEntity<>(
            "Company successfully created",
            HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        try {
            companyService.updateCompanyById(id, company);
            return new ResponseEntity<>(
                "Company updated successfully",
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                "Company not found!",
                HttpStatus.NOT_FOUND
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Long id) {
        boolean isDeleted = companyService.deleteCompany(id);
        if (isDeleted) {
            return new ResponseEntity<>(
                "Company Deleted Successfully",
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                "Company Not found",
                HttpStatus.NOT_FOUND
            );
        }
    }
}
