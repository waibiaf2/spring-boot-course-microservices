package com.yonderone.companyms.company;

import com.yonderone.companyms.dto.CompanyWithReviewsDTO;
import com.yonderone.companyms.dto.ReviewMessage;

import java.util.List;

public interface CompanyService {
    List<CompanyWithReviewsDTO> findAll();
    Company updateCompanyById(Long id, Company updatedCompany);
    Company getCompanyById(Long id);
    void  createCompany(Company company);
    boolean deleteCompany(Long id);

    public void updateCompanyRating(ReviewMessage reviewMessage);
}
