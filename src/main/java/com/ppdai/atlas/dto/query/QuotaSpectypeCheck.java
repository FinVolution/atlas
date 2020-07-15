package com.ppdai.atlas.dto.query;

import lombok.Data;

import java.util.List;

@Data
public class QuotaSpectypeCheck {
    private List<SpecTypeItem> usedSpecItems;
    private List<SpecTypeItem> applySpecItems;
}
