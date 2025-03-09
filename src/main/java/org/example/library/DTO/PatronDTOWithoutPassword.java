package org.example.library.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.library.Models.BorrowingRecord;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatronDTOWithoutPassword {
    private Long id;
    private String email;
    private String name;
    private List<BorrowingRecord> borrowingRecords;
}
