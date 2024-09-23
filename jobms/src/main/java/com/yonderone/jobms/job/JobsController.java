package com.yonderone.jobms.job;

import com.yonderone.jobms.dto.JobDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobsController {
    private final JobService jobService;

    public JobsController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> findAll() {
        /*return ResponseEntity.ok(jobService.getJobs());*/
        return new ResponseEntity<>(jobService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Job job) {
        jobService.createJob(job);
        /*return ResponseEntity.status(HttpStatus.CREATED).body("Job created");*/
        return new ResponseEntity<>("Job created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> findJobById(@PathVariable Long id) {
        JobDTO job = jobService.getJobById(id);

        if (job == null) {
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(job);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        boolean deleted = jobService.deleteJobById(id);

        if (deleted) {
            return new ResponseEntity<>(
                    "Job deleted successfully",
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(
                "Job not found",
                HttpStatus.NOT_FOUND
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable Long id, @RequestBody Job updatedJob) {
        boolean updated = jobService.updateJob(id, updatedJob);
        if (updated) {
            return new ResponseEntity<>("Job updated successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
    }
}
