package com.yonderone.jobms.job.mapper;

import com.yonderone.jobms.dto.JobDTO;
import com.yonderone.jobms.external.Company;
import com.yonderone.jobms.external.Review;
import com.yonderone.jobms.job.Job;

import java.util.List;

public class JobMapper {
    public static JobDTO mapToJobWithCompanyDto(
        Job job,
        Company company,
        List<Review> reviews
    ) {
        JobDTO jobDTO = new JobDTO();

        jobDTO.setId(job.getId());
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setMaxSalary(job.getMaxSalary());
        jobDTO.setLocation(job.getLocation());

        jobDTO.setReviews(reviews);

        jobDTO.setCompany(company);

        return jobDTO;
    }
}
