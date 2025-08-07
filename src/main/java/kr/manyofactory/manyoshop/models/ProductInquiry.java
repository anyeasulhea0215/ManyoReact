package kr.manyofactory.manyoshop.models;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductInquiry {
          
    private int inquiryId; 
    private int memberId;           
          

    private String inquiryType;     
    private String title;            
    private String content;          
    private String answer;          
    private String attachedFileUrl;  

    private LocalDateTime regDate;  
    private LocalDateTime answerDate; 
    private String isAnswered; 
    
    private Integer productId;


    
}
