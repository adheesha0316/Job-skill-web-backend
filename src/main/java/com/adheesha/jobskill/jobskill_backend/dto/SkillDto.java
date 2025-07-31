package com.adheesha.jobskill.jobskill_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkillDto {
    private Integer skillId;
    private String skillName;
    private Integer seekerId;
}
