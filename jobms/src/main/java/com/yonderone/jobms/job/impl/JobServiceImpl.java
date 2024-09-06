package com.yonderone.jobms.job.impl;


import com.yonderone.jobms.dto.JobWithCompanyDTO;
import com.yonderone.jobms.external.Company;
import com.yonderone.jobms.job.Job;
import com.yonderone.jobms.job.JobRepository;
import com.yonderone.jobms.job.JobService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();

        return jobs.stream()
            .map(this::covertToDto)
            .collect(Collectors.toList());
    }

    private JobWithCompanyDTO covertToDto(Job job) {
        JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
        RestTemplate restTemplate = new RestTemplate();
        jobWithCompanyDTO.setJob(job);

        Company company = restTemplate.getForObject(
            "http://localhost:8081/companies/" + job.getCompanyId(),
            Company.class
        );

        jobWithCompanyDTO.setCompany(company);

        return jobWithCompanyDTO;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteJobById(Long id) {
        boolean jobExits = jobRepository.existsById(id);
        if (jobExits) {
            jobRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);

        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();

            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setLocation(updatedJob.getLocation());

            jobRepository.save(job);

            return true;
        }

        return false;
    }
}
