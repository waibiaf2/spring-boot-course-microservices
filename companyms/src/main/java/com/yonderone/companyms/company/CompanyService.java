package com.yonderone.companyms.company;

import com.yonderone.companyms.company.dto.CompanyWithReviewsDTO;
import com.yonderone.companyms.company.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<CompanyWithReviewsDTO> findAll();
    void updateCompanyById(Long id, Company updatedCompany);
    Company getCompanyById(Long id);
    void  createCompany(Company company);
    boolean deleteCompany(Long id);
    void updateCompanyRating(ReviewMessage reviewMessage);
}
