import java.time.LocalDateTime;
 public class beginAndDueDates{
     LocalDateTime beginDate;
     LocalDateTime dueDate;

     public void setBeginDate(LocalDateTime beginDate){
         this.beginDate = beginDate;
     }

     public void setDueDate(LocalDateTime dueDate){
         this.dueDate = dueDate;
     }

     public LocalDateTime getBeginDate(){
         return beginDate;
     }

     public LocalDateTime getDueDate(){
         return dueDate;
     }
 }
