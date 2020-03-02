package uk.gov.dvla.osl.addresslookup.common;

import uk.gov.dvla.osl.commons.AddressPAF;

import java.util.List;

public interface IAddressLookupService
{
    List<AddressPAF> getAddressList(final String postcode);
}
