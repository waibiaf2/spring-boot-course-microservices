package com.yonderone.jobms.job.impl;

import com.yonderone.jobms.dto.JobDTO;
import com.yonderone.jobms.external.Company;
import com.yonderone.jobms.external.Review;
import com.yonderone.jobms.job.Job;
import com.yonderone.jobms.job.JobRepository;
import com.yonderone.jobms.job.JobService;
import com.yonderone.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    int attempt = 0;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    /*@CircuitBreaker(name= "companyBreaker", fallbackMethod = "companyBreakerFallback")*/
    /*@Retry(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")*/
    @RateLimiter(name = "companyBreaker", fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        System.out.println("Attempt: " + ++attempt);
        List<Job> jobs = jobRepository.findAll();

        return jobs.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    public List<String> companyBreakerFallback(Exception e) {
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);

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

    private JobDTO convertToDto(Job job) {

        Company company = restTemplate.getForObject(
            "http://GATEWAY/companies/" + job.getCompanyId(),
            Company.class
        );

        ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
            "http://GATEWAY/reviews?companyId=" + job.getCompanyId(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Review>>() {}
        );

        List<Review> reviews = reviewResponse.getBody();

        JobDTO jobDTO;
        jobDTO = JobMapper.mapToJobWithCompanyDto(job, company, reviews);

        return jobDTO;
    }
}
