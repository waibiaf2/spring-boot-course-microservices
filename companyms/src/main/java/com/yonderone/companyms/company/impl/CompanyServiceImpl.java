package com.yonderone.companyms.company.impl;

import com.yonderone.companyms.company.Company;
import com.yonderone.companyms.company.CompanyRepository;
import com.yonderone.companyms.company.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company updateCompanyById(Long id, Company company) {
        Optional<Company> companyOptional = companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            Company updatedCompany = companyOptional.get();

            updatedCompany.setName(company.getName());
            updatedCompany.setDescription(company.getDescription());

            companyRepository.save(updatedCompany);
            return updatedCompany;
        }

        return null;
    }

    public boolean deleteCompany(Long id) {
        if(companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }else
            return false;
    }
}
