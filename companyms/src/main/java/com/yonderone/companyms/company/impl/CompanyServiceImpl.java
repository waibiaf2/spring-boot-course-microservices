package com.yonderone.companyms.company.impl;

import com.yonderone.companyms.company.Company;
import com.yonderone.companyms.company.CompanyRepository;
import com.yonderone.companyms.company.CompanyService;
import com.yonderone.companyms.company.client.ReviewClient;
import com.yonderone.companyms.company.dto.CompanyWithReviewsDTO;
import com.yonderone.companyms.company.dto.ReviewMessage;
import com.yonderone.companyms.external.Review;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final ReviewClient reviewClient;

    @Autowired
    RestTemplate restTemplate;

    public CompanyServiceImpl(
        CompanyRepository companyRepository,
        ReviewClient reviewClient
    ) {
        this.companyRepository = companyRepository;
        this.reviewClient = reviewClient;
    }

    public List<CompanyWithReviewsDTO> findAll() {
       List<Company> companies =  companyRepository.findAll();

       return companies.stream()
           .map(this::covertToDto)
           .collect(Collectors.toList());
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
    public void updateCompanyById(Long id, Company company) {
        Optional<Company> companyOptional = companyRepository.findById(id);

        if (companyOptional.isPresent()) {
            Company updatedCompany = companyOptional.get();

            updatedCompany.setName(company.getName());
            updatedCompany.setDescription(company.getDescription());

            companyRepository.save(updatedCompany);
        }

    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        System.out.println(reviewMessage.getDescription());

        Company company = companyRepository
                .findById(reviewMessage.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found" + reviewMessage.getCompanyId()));

        Double averageRating = reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating((double) Math.round(averageRating));
        companyRepository.save(company);

    }

    public boolean deleteCompany(Long id) {
        if(companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }else
            return false;
    }

    private CompanyWithReviewsDTO covertToDto(Company company) {
        CompanyWithReviewsDTO companyWithReviewsDTO = new CompanyWithReviewsDTO();

        companyWithReviewsDTO.setCompany(company);

        List<Review> reviews = restTemplate.getForObject(
            "http://GATEWAY/reviews?companyId=" + company.getId(),
            List.class
        );

        companyWithReviewsDTO.setReviews(reviews);

        return companyWithReviewsDTO;
    }
}
