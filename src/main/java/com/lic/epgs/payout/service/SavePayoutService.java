package com.lic.epgs.payout.service;

import com.lic.epgs.payout.entity.PayoutEntity;
import com.lic.epgs.payout.temp.entity.TempPayoutEntity;

public interface SavePayoutService {

	PayoutEntity insert(String payoutNo);

	PayoutEntity insertNew(TempPayoutEntity tempPayoutEntityUpdated);

}