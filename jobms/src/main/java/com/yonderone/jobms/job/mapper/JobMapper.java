package com.yonderone.jobms.job.mapper;

import com.yonderone.jobms.dto.JobWithCompanyDTO;
import com.yonderone.jobms.external.Company;
import com.yonderone.jobms.job.Job;

public class JobMapper {
    public static JobWithCompanyDTO mapToJobWithCompanyDto(Job job, Company company) {
        JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();

        jobWithCompanyDTO.setId(job.getId());
        jobWithCompanyDTO.setTitle(job.getTitle());
        jobWithCompanyDTO.setDescription(job.getDescription());
        jobWithCompanyDTO.setMaxSalary(job.getMaxSalary());
        jobWithCompanyDTO.setMaxSalary(job.getMaxSalary());
        jobWithCompanyDTO.setLocation(job.getLocation());

        jobWithCompanyDTO.setCompany(company);

        return jobWithCompanyDTO;
    }
}
