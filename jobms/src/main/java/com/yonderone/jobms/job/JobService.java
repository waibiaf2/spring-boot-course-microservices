package com.yonderone.jobms.job;

import com.yonderone.jobms.job.dto.JobDTO;

import java.util.List;

public interface JobService {
    List<JobDTO> findAllJobs();
    void createJob(Job job);
    JobDTO getJobById(Long id);
    boolean deleteJobById(Long id);
    boolean updateJob(Long id, Job updatedJob);
}
