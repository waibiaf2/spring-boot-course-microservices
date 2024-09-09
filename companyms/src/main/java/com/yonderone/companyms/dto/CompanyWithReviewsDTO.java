package com.yonderone.companyms.dto;

import com.yonderone.companyms.company.Company;
import com.yonderone.companyms.external.Review;

import java.util.List;

public class CompanyWithReviewsDTO {
    private Company company;
    private List<Review> reviews;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
