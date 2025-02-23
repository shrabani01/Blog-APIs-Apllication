package com.blogApplication.app.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDao {
	
    private Integer categoryId;
    
    @NotBlank
    @Size(min=6 ,message="Category title must contain min 6 characters")
	private String categoryTitle;
    
    @NotBlank
    @Size(min=10 ,message="Category description must contain min 10 characters")
	private String categoryDescription;

}
