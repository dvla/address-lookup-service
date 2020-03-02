package uk.gov.dvla.osl.addresslookup.api.os;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;


/**
 * Create Dealer Response.
 */

@Value
@Builder
@AllArgsConstructor(access= AccessLevel.PUBLIC)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=Id.NAME)
public class DPA
{
    @JsonProperty("UPRN")
    private String uprn;

    @JsonProperty("UDPRN")
    private String udprn;

    @JsonProperty("ADDRESS")
    private String address;

    @JsonProperty("PO_BOX_NUMBER")
    private String poBoxNumber;

    @JsonProperty("ORGANISATION_NAME")
    private String organisationName;

    @JsonProperty("DEPARTMENT_NAME")
    private String departmentName;

    @JsonProperty("SUB_BUILDING_NAME")
    private String subBuildingName;

    @JsonProperty("BUILDING_NAME")
    private String buildingName;

    @JsonProperty("BUILDING_NUMBER")
    private String buildingNumber;

    @JsonProperty("DEPENDENT_THOROUGHFARE_NAME")
    private String dependentThoroughfareName;

    @JsonProperty("THOROUGHFARE_NAME")
    private String thoroughfareName;

    @JsonProperty("DOUBLE_DEPENDENT_LOCALITY")
    private String doubleDependentLocality;

    @JsonProperty("DEPENDENT_LOCALITY")
    private String dependentLocality;

    @JsonProperty("POST_TOWN")
    private String postTown;

    @JsonProperty("POSTCODE")
    private String postCode;

    @JsonProperty("RPC")
    private String rpc;

    @JsonProperty("X_COORDINATE")
    private String xCoordinate;

    @JsonProperty("Y_COORDINATE")
    private String yCoordinate;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("MATCH")
    private String match;

    @JsonProperty("MATCH_DESCRIPTION")
    private String matchDescription;

    @JsonProperty("LANGUAGE")
    private String language;

    @JsonProperty("LOCAL_CUSTODIAN_CODE")
    private String localCustodianCode;

    @JsonProperty("LOCAL_CUSTODIAN_CODE_DESCRIPTION")
    private String localCustodianCodeDescription;

    @JsonProperty("CLASSIFICATION_CODE")
    private String classificationCode;

    @JsonProperty("CLASSIFICATION_CODE_DESCRIPTION")
    private String classificationCodeDescription;

    @JsonProperty("POSTAL_ADDRESS_CODE")
    private String postalAddressCode;

    @JsonProperty("POSTAL_ADDRESS_CODE_DESCRIPTION")
    private String postalAddressCodeDescription;

    @JsonProperty("LOGICAL_STATUS_CODE")
    private String logicalStatusCode;

    @JsonProperty("BLPU_STATE_CODE")
    private String blpuStateCode;

    @JsonProperty("BLPU_STATE_CODE_DESCRIPTION")
    private String blpuStateCodeDescription;

    @JsonProperty("TOPOGRAPHY_LAYER_TOID")
    private String topographyLayerToId;

    @JsonProperty("PARENT_UPRN")
    private String parentUPRN;

    @JsonProperty("LAST_UPDATE_DATE")
    private String lastUpdateDate;

    @JsonProperty("ENTRY_DATE")
    private String entryDate;

    @JsonProperty("LEGAL_NAME")
    private String legalName;

    @JsonProperty("BLPU_STATE_DATE")
    private String blpuStateDate;

}
