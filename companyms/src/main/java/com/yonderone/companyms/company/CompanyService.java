package com.yonderone.companyms.company;

import java.util.List;

public interface CompanyService {
    List<Company> findAll();
    Company updateCompanyById(Long id, Company updatedCompany);
    Company getCompanyById(Long id);
    void  createCompany(Company company);
    boolean deleteCompany(Long id);
}
