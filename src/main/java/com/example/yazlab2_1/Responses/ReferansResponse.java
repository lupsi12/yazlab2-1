package com.example.yazlab2_1.Responses;

import com.example.yazlab2_1.Entities.Referans;
import com.example.yazlab2_1.Entities.Veri;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferansResponse {
   private Long id;
   private String referans;
   Long veriId;
   String veriYayinAd;
   public ReferansResponse(Referans referansEntity) {
      this.id = referansEntity.getId();
      this.referans = referansEntity.getReferans();
      this.veriId = referansEntity.getVeri().getId();
      this.veriYayinAd = referansEntity.getVeri().getYayinAd();
   }
}
