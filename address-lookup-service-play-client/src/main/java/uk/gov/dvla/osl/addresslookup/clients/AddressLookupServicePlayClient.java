package uk.gov.dvla.osl.addresslookup.clients;

import uk.gov.dvla.osl.addresslookup.common.IAddressLookupService;
import uk.gov.dvla.osl.commons.AddressPAF;

import java.util.ArrayList;
import java.util.List;

public class AddressLookupServicePlayClient implements IAddressLookupService
{
    @Override
    public List<AddressPAF> getAddressList(final String postcode) {
        return new ArrayList<>();
    }
}
