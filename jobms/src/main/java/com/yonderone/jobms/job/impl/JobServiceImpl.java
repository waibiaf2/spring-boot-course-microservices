package com.yonderone.jobms.job.impl;


import com.yonderone.jobms.dto.JobWithCompanyDTO;
import com.yonderone.jobms.external.Company;
import com.yonderone.jobms.job.Job;
import com.yonderone.jobms.job.JobRepository;
import com.yonderone.jobms.job.JobService;
import com.yonderone.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();

        return jobs.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());

    }

    @Override
    public JobWithCompanyDTO getJobById(Long id) {
        Job job =  jobRepository.findById(id).orElse(null);

        if (job == null) {
            return null;
        }

        return convertToDto(job);
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
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

    private JobWithCompanyDTO convertToDto(Job job) {

        Company company = restTemplate.getForObject(
            "http://COMPANY-SERVICE/companies/" + job.getCompanyId(),
            Company.class
        );

        JobWithCompanyDTO jobWithCompanyDTO;
        jobWithCompanyDTO = JobMapper.mapToJobWithCompanyDto(job, company);

        return jobWithCompanyDTO;
    }
}
