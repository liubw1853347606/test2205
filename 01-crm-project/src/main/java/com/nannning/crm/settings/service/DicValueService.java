package com.nannning.crm.settings.service;

import com.nannning.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueService {
    List<DicValue> queryDicValueByTypeCode(String typeCode);
}
