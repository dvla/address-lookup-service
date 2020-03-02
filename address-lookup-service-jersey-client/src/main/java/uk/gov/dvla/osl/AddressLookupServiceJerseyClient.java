package uk.gov.dvla.osl;

import uk.gov.dvla.osl.addresslookup.common.IAddressLookupService;
import uk.gov.dvla.osl.commons.AddressPAF;

import java.util.ArrayList;
import java.util.List;

public class AddressLookupServiceJerseyClient  implements IAddressLookupService
{
    @Override
    public List<AddressPAF> getAddressList(final String postcode) {
        return new ArrayList<>();
    }
}
