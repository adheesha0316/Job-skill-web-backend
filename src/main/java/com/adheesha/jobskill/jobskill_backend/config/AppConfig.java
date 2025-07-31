package com.adheesha.jobskill.jobskill_backend.config;

import com.adheesha.jobskill.jobskill_backend.dto.JobSeekerDto;
import com.adheesha.jobskill.jobskill_backend.dto.TrainerDto;
import com.adheesha.jobskill.jobskill_backend.entity.JobSeeker;
import com.adheesha.jobskill.jobskill_backend.entity.Trainer;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping: JobSeeker → JobSeekerDto
        modelMapper.addMappings(new PropertyMap<JobSeeker, JobSeekerDto>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getUserId());
            }
        });

        // Optional: JobSeekerDto → JobSeeker (skip user; you manually set in service)
        modelMapper.addMappings(new PropertyMap<JobSeekerDto, JobSeeker>() {
            @Override
            protected void configure() {
                skip(destination.getUser()); // You will manually set user later
            }
        });

        // Trainer → TrainerDto
        modelMapper.addMappings(new PropertyMap<Trainer, TrainerDto>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getUserId());
            }
        });

        // TrainerDto → Trainer
        modelMapper.addMappings(new PropertyMap<TrainerDto, Trainer>() {
            @Override
            protected void configure() {
                skip(destination.getUser());
            }
        });


        return new ModelMapper();
    }
}
