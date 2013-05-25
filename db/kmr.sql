/*
 Navicat Premium Data Transfer

 Source Server         : 172.31.5.82
 Source Server Type    : MySQL
 Source Server Version : 50161
 Source Host           : 172.31.5.82
 Source Database       : kmr

 Target Server Type    : MySQL
 Target Server Version : 50161
 File Encoding         : utf-8

 Date: 01/04/2013 21:21:11 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for ACL_Permission
-- ----------------------------
DROP TABLE IF EXISTS ACL_Permission;
CREATE TABLE ACL_Permission (
  ACL_ID int(11) NOT NULL,
  UR_ID int(11) NOT NULL,
  UR_Permission varchar(255) DEFAULT NULL,
  PRIMARY KEY (ACL_ID,UR_ID),
  KEY FKA72DF4E4C327BF5 (UR_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for DZ_APPT
-- ----------------------------
DROP TABLE IF EXISTS DZ_APPT;
CREATE TABLE DZ_APPT (
  PATID bigint(20) NOT NULL,
  APPOINTMENT_ID bigint(20) DEFAULT NULL,
  APPOINTMENT_STATUS_ID decimal(19,2) DEFAULT NULL,
  APPTDATE date NOT NULL,
  DATA_SOURCE varchar(255) NOT NULL,
  DISEASETYPE_ID int(11) DEFAULT NULL,
  FLAG int(11) DEFAULT NULL,
  INSERTED_DT date DEFAULT NULL,
  PCMID bigint(20) DEFAULT NULL,
  POCID bigint(20) DEFAULT NULL,
  PRIMARY KEY (PATID),
  KEY FKAC15607C2D1F85DC (POCID),
  KEY FKAC15607C2BFB5D73 (PATID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for DZ_PATIENTS
-- ----------------------------
DROP TABLE IF EXISTS DZ_PATIENTS;
CREATE TABLE DZ_PATIENTS (
  PATID bigint(20) NOT NULL,
  ACV varchar(255) DEFAULT NULL,
  ADDRESS varchar(255) DEFAULT NULL,
  CITY varchar(255) DEFAULT NULL,
  DATA_SOURCE varchar(255) NOT NULL,
  DECEASED_DT date DEFAULT NULL,
  DMIS varchar(255) DEFAULT NULL,
  DOB varchar(255) DEFAULT NULL,
  END_ENROLLED_DT datetime DEFAULT NULL,
  ENROLLED char(1) DEFAULT NULL,
  FLAG int(11) DEFAULT NULL,
  FMPSSN varchar(255) DEFAULT NULL,
  INACTIVE_DT datetime DEFAULT NULL,
  INSERTED_DT datetime DEFAULT NULL,
  LAST_APPT_DT date DEFAULT NULL,
  MCP varchar(255) DEFAULT NULL,
  MCPID bigint(20) DEFAULT NULL,
  NAME varchar(255) DEFAULT NULL,
  NED_HCDP_CODE varchar(255) DEFAULT NULL,
  NED_HCDP_TEXT varchar(255) DEFAULT NULL,
  OFFICE_PHONE varchar(255) DEFAULT NULL,
  PATIENT_CATEGORY_STATUS varchar(255) DEFAULT NULL,
  PATIENT_DEERS_IDENTIFIER bigint(20) DEFAULT NULL,
  PATIENT_DOB datetime DEFAULT NULL,
  pcmid bigint(20) DEFAULT NULL,
  PHONE varchar(255) DEFAULT NULL,
  pocid bigint(20) DEFAULT NULL,
  PREV_DMIS varchar(255) DEFAULT NULL,
  PREV_ENROLLED char(1) DEFAULT NULL,
  PREV_ENROLLED_DT datetime DEFAULT NULL,
  PREV_PCMID bigint(20) DEFAULT NULL,
  PREV_POCID bigint(20) DEFAULT NULL,
  START_ENROLLED_DT datetime DEFAULT NULL,
  STATE varchar(255) DEFAULT NULL,
  STREET2 varchar(255) DEFAULT NULL,
  UNIT_SHIP_ID int(11) DEFAULT NULL,
  UNIT_SHIP_NAME varchar(255) DEFAULT NULL,
  UPDATED varchar(255) DEFAULT NULL,
  UPDATED_DT datetime DEFAULT NULL,
  ZIP varchar(255) DEFAULT NULL,
  PRIMARY KEY (PATID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for DZ_PCM
-- ----------------------------
DROP TABLE IF EXISTS DZ_PCM;
CREATE TABLE DZ_PCM (
  PCMID bigint(20) NOT NULL,
  DATA_SOURCE varchar(255) NOT NULL,
  FLAG int(11) DEFAULT NULL,
  HOSPITAL_LOCATION_ID decimal(19,2) DEFAULT NULL,
  INSERTED_DT date DEFAULT NULL,
  PCM varchar(255) DEFAULT NULL,
  PROVIDER_ID_CODE varchar(255) DEFAULT NULL,
  PROVIDER_SIDR varchar(255) DEFAULT NULL,
  PRIMARY KEY (PCMID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for DZ_POC
-- ----------------------------
DROP TABLE IF EXISTS DZ_POC;
CREATE TABLE DZ_POC (
  POCID bigint(20) NOT NULL,
  DATA_SOURCE varchar(255) NOT NULL,
  DIVISION_DMIS varchar(255) DEFAULT NULL,
  DIVISION_ID int(11) DEFAULT NULL,
  FLAG int(11) DEFAULT NULL,
  HOSPITAL_LOCATION_INACTIVE_DT date DEFAULT NULL,
  INSERTED_DT date DEFAULT NULL,
  POC varchar(255) DEFAULT NULL,
  PRIMARY KEY (POCID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for DZ_REG
-- ----------------------------
DROP TABLE IF EXISTS DZ_REG;
CREATE TABLE DZ_REG (
  DISEASETYPE_ID int(11) NOT NULL,
  PATID bigint(20) NOT NULL,
  ACTIVE int(11) NOT NULL,
  CMD_FLAG_DT date DEFAULT NULL,
  CYCLE1_SCENARIO int(11) DEFAULT NULL,
  DATA_SOURCE varchar(255) NOT NULL,
  FLAG int(11) DEFAULT NULL,
  HEDIS int(11) DEFAULT NULL,
  INSERTED_DT datetime DEFAULT NULL,
  REGISTERED_DT datetime DEFAULT NULL,
  PRIMARY KEY (DISEASETYPE_ID,PATID),
  KEY FK792A394BE6612C65 (DISEASETYPE_ID),
  KEY FK792A394B2BFB5D73 (PATID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for DZ_TYPE
-- ----------------------------
DROP TABLE IF EXISTS DZ_TYPE;
CREATE TABLE DZ_TYPE (
  DZTYPE_ID int(11) NOT NULL,
  DESCR varchar(255) DEFAULT NULL,
  INSERTED_DT date DEFAULT NULL,
  PRIMARY KEY (DZTYPE_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for FactSpecification
-- ----------------------------
DROP TABLE IF EXISTS FactSpecification;
CREATE TABLE FactSpecification (
  FD_ID int(11) NOT NULL,
  TerminologyCode varchar(255) NOT NULL,
  TerminologyScheme varchar(255) NOT NULL,
  Comments varchar(255) DEFAULT NULL,
  PRIMARY KEY (FD_ID,TerminologyCode,TerminologyScheme)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for KMV_AccessControlList
-- ----------------------------
DROP TABLE IF EXISTS KMV_AccessControlList;
CREATE TABLE KMV_AccessControlList (
  ACL_ID int(11) NOT NULL AUTO_INCREMENT,
  ACL_Name varchar(50) NOT NULL,
  ACL_Description varchar(255) DEFAULT NULL,
  PRIMARY KEY (ACL_ID),
  UNIQUE KEY ACL_Name (ACL_Name)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for KMV_InferenceEngineDependency
-- ----------------------------
DROP TABLE IF EXISTS KMV_InferenceEngineDependency;
CREATE TABLE KMV_InferenceEngineDependency (
  KMV_ID int(11) NOT NULL,
  Comments varchar(255) DEFAULT NULL,
  TerminologyCode varchar(255) NOT NULL,
  TerminologyScheme varchar(255) NOT NULL,
  PRIMARY KEY (KMV_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for KMV_SupportingReference
-- ----------------------------
DROP TABLE IF EXISTS KMV_SupportingReference;
CREATE TABLE KMV_SupportingReference (
  SR_ID int(11) NOT NULL,
  KMV_ID int(11) NOT NULL,
  SR_Description varchar(255) DEFAULT NULL,
  SR_Document blob,
  SR_Name varchar(255) DEFAULT NULL,
  SR_Reference varchar(255) DEFAULT NULL,
  SR_Type varchar(255) DEFAULT NULL,
  PRIMARY KEY (SR_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for KMV_TaskDependency
-- ----------------------------
DROP TABLE IF EXISTS KMV_TaskDependency;
CREATE TABLE KMV_TaskDependency (
  TT_ID int(11) NOT NULL AUTO_INCREMENT,
  KMV_ID int(11) DEFAULT NULL,
  TD_Type varchar(50) NOT NULL,
  PRIMARY KEY (TT_ID),
  KEY KMV_ID (KMV_ID),
  KEY TD_Type (TD_Type),
  CONSTRAINT FK_KMV_TaskDependency_KM_Version FOREIGN KEY (KMV_ID) REFERENCES KM_Version (KMV_ID)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for KMV_UsageStatistic
-- ----------------------------
DROP TABLE IF EXISTS KMV_UsageStatistic;
CREATE TABLE KMV_UsageStatistic (
  UST_ID int(11) NOT NULL,
  KMV_ID int(11) NOT NULL,
  UST_Value varchar(255) DEFAULT NULL,
  PRIMARY KEY (UST_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for KM_Specialty
-- ----------------------------
DROP TABLE IF EXISTS KM_Specialty;
CREATE TABLE KM_Specialty (
  KM_ID int(11) NOT NULL,
  TerminologyCode varchar(255) NOT NULL,
  TerminologyScheme varchar(255) NOT NULL,
  Comments varchar(255) DEFAULT NULL,
  PRIMARY KEY (KM_ID,TerminologyCode,TerminologyScheme)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for KM_Version
-- ----------------------------
DROP TABLE IF EXISTS KM_Version;
CREATE TABLE KM_Version (
  KMV_ID int(11) NOT NULL AUTO_INCREMENT,
  KM_ID int(11) NOT NULL,
  KM_VersionNum int(11) DEFAULT NULL,
  KMV_Name varchar(50) DEFAULT NULL,
  ACL_ID int(11) DEFAULT NULL,
  LMT_ID int(11) DEFAULT NULL,
  ValidationStatus varchar(50) DEFAULT NULL COMMENT Status of this version of the KM. eg: draft; under-review; approved; QA-passed; production,
  Logic_IntermediateForm longtext COMMENT Intermediate or text form of the inference logic of this version of the KM as represented by the inference engine, for example, the .drl file content of the Drools rule engine for this KM.,
  Logic_NativeForm longtext COMMENT Native representation of inference logic by the inference engine,
  Logic_BinaryForm blob COMMENT Binary form of the inference logic as used in the inference engine,
  AuthorComments text,
  LastModifiedTimestamp datetime DEFAULT NULL,
  CreatedBy_AuthorID varchar(50) DEFAULT NULL,
  CreatedBy_AuthorName varchar(50) DEFAULT NULL,
  CreatedTimestamp datetime DEFAULT NULL,
  PRIMARY KEY (KMV_ID),
  KEY ACL_ID (ACL_ID),
  KEY FK_KM_Version_KnowledgeModule (KM_ID),
  CONSTRAINT FK_KM_Version_KMV_AccessControlList FOREIGN KEY (ACL_ID) REFERENCES KMV_AccessControlList (ACL_ID),
  CONSTRAINT FK_KM_Version_KnowledgeModule FOREIGN KEY (KM_ID) REFERENCES KnowledgeModule (KM_ID)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for KnowledgeModule
-- ----------------------------
DROP TABLE IF EXISTS KnowledgeModule;
CREATE TABLE KnowledgeModule (
  KM_ID int(11) NOT NULL AUTO_INCREMENT,
  KM_Name varchar(50) NOT NULL,
  KM_Type varchar(50) DEFAULT NULL COMMENT Type of KM. eg: workflow, guideline, validation rule.,
  KM_OrganizationLevel varchar(50) DEFAULT NULL COMMENT Highest organization level this KM is applicable. eg: institution, local, personal ,
  KM_Keywords varchar(255) DEFAULT NULL,
  KM_Description varchar(255) DEFAULT NULL,
  Parent_KM_ID int(11) DEFAULT NULL,
  OriginInstitution varchar(50) DEFAULT NULL,
  CreatedBy_AuthorID varchar(50) DEFAULT NULL,
  CreatedBy_AuthorName varchar(50) DEFAULT NULL,
  CreatedTimestamp datetime DEFAULT NULL,
  LatestVersionNum int(11) DEFAULT NULL,
  LastModifiedBy_AuthorID varchar(50) DEFAULT NULL,
  LastModifiedBy_AuthorName varchar(50) DEFAULT NULL,
  LastModifiedTimestamp datetime DEFAULT NULL,
  ProductionVersionNum int(11) DEFAULT NULL COMMENT Version of KM that is in production (VersionNum from KM_Version table),
  ValidationStatus varchar(50) DEFAULT NULL COMMENT Status of this KM. eg: draft; under-review; approved; QA-passed; production,
  isCheckedOut tinyint(1) DEFAULT NULL,
  PRIMARY KEY (KM_ID),
  UNIQUE KEY KM_Name (KM_Name)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for OperationalConstraint
-- ----------------------------
DROP TABLE IF EXISTS OperationalConstraint;
CREATE TABLE OperationalConstraint (
  OC_ID int(11) NOT NULL,
  OC_Description varchar(255) DEFAULT NULL,
  OC_Keywords varchar(255) DEFAULT NULL,
  OC_Name varchar(255) NOT NULL,
  OC_Type varchar(255) DEFAULT NULL,
  PRIMARY KEY (OC_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for OperationalConstraintElement
-- ----------------------------
DROP TABLE IF EXISTS OperationalConstraintElement;
CREATE TABLE OperationalConstraintElement (
  OCE_ID int(11) NOT NULL,
  OCE_AttributeName varchar(255) DEFAULT NULL,
  OCE_AttributeValues varchar(255) DEFAULT NULL,
  OCE_Comparator varchar(255) DEFAULT NULL,
  OCE_Description varchar(255) DEFAULT NULL,
  OCE_Interpretation varchar(255) DEFAULT NULL,
  OCE_Name varchar(255) DEFAULT NULL,
  OCE_Type varchar(255) DEFAULT NULL,
  OCE_UnitOfMeasure varchar(255) DEFAULT NULL,
  OC_ID int(11) NOT NULL,
  PRIMARY KEY (OCE_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for PopulationSpecification
-- ----------------------------
DROP TABLE IF EXISTS PopulationSpecification;
CREATE TABLE PopulationSpecification (
  BP_ID int(11) NOT NULL,
  TerminologyCode varchar(255) NOT NULL,
  TerminologyScheme varchar(255) NOT NULL,
  TerminologyValue varchar(255) NOT NULL,
  Type varchar(255) DEFAULT NULL,
  PRIMARY KEY (BP_ID,TerminologyCode,TerminologyScheme,TerminologyValue)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for RefCodeSimple
-- ----------------------------
DROP TABLE IF EXISTS RefCodeSimple;
CREATE TABLE RefCodeSimple (
  name varchar(255) NOT NULL,
  description varchar(255) DEFAULT NULL,
  PRIMARY KEY (name)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for TT_Specification
-- ----------------------------
DROP TABLE IF EXISTS TT_Specification;
CREATE TABLE TT_Specification (
  TT_ID int(11) NOT NULL,
  TerminologyScheme varchar(50) NOT NULL,
  TerminologyCode varchar(50) NOT NULL,
  Comments varchar(255) DEFAULT NULL,
  PRIMARY KEY (TT_ID,TerminologyScheme,TerminologyCode),
  CONSTRAINT FK_TT_Specification_TaskType FOREIGN KEY (TT_ID) REFERENCES KMV_TaskDependency (TT_ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for UserRole
-- ----------------------------
DROP TABLE IF EXISTS UserRole;
CREATE TABLE UserRole (
  UR_ID int(11) NOT NULL AUTO_INCREMENT,
  UR_Name varchar(50) NOT NULL,
  UR_Description varchar(255) DEFAULT NULL,
  PRIMARY KEY (UR_ID),
  UNIQUE KEY UR_Name (UR_Name)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
--  Table structure for addrbook
-- ----------------------------
DROP TABLE IF EXISTS addrbook;
CREATE TABLE addrbook (
  ADDR_ID bigint(20) NOT NULL,
  User_ID varchar(50) NOT NULL,
  Class_ID varchar(50) NOT NULL,
  Name varchar(100) DEFAULT NULL,
  Contact_ID varchar(100) DEFAULT NULL,
  Last_Updated datetime NOT NULL,
  PRIMARY KEY (ADDR_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for alertaction
-- ----------------------------
DROP TABLE IF EXISTS alertaction;
CREATE TABLE alertaction (
  AA_ActionID bigint(20) NOT NULL AUTO_INCREMENT,
  ProviderID varchar(100) DEFAULT NULL,
  ProviderName varchar(100) DEFAULT NULL,
  Name varchar(50) DEFAULT NULL,
  Message varchar(255) DEFAULT NULL,
  Timestamp datetime NOT NULL,
  AT_TicketID bigint(20) NOT NULL,
  UserID varchar(100) DEFAULT NULL,
  UserName varchar(100) DEFAULT NULL,
  UserProvider bit(1) DEFAULT NULL,
  PRIMARY KEY (AA_ActionID),
  KEY FKDE6B92929C17FB6D (AT_TicketID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for alertcontact
-- ----------------------------
DROP TABLE IF EXISTS alertcontact;
CREATE TABLE alertcontact (
  AT_ContactID bigint(20) NOT NULL AUTO_INCREMENT,
  Method varchar(50) DEFAULT NULL,
  UserID varchar(100) DEFAULT NULL,
  UserName varchar(100) DEFAULT NULL,
  UserLDAP varchar(100) DEFAULT NULL,
  ClinicID varchar(50) DEFAULT NULL,
  OrganizationID varchar(50) DEFAULT NULL,
  Role varchar(100) DEFAULT NULL,
  Location varchar(100) DEFAULT NULL,
  AT_TicketID bigint(20) NOT NULL,
  PRIMARY KEY (AT_ContactID),
  KEY FK6CFD9B449C17FB6D (AT_TicketID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for alertstatus
-- ----------------------------
DROP TABLE IF EXISTS alertstatus;
CREATE TABLE alertstatus (
  AT_StatusID bigint(20) NOT NULL AUTO_INCREMENT,
  UserId varchar(50) DEFAULT NULL,
  CreateTimeStamp datetime NOT NULL,
  Flagged bit(1) DEFAULT b'0',
  Archive bit(1) DEFAULT b'0',
  Deleted bit(1) DEFAULT b'0',
  AT_TicketID bigint(20) NOT NULL,
  PRIMARY KEY (AT_StatusID),
  KEY FKFE09E44E9C17FB6D (AT_TicketID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for alertticket
-- ----------------------------
DROP TABLE IF EXISTS alertticket;
CREATE TABLE alertticket (
  AT_TicketID bigint(20) NOT NULL AUTO_INCREMENT,
  TicketUniqueId varchar(100) NOT NULL,
  AT_ID bigint(20) NOT NULL,
  Type varchar(50) DEFAULT NULL,
  Description varchar(255) DEFAULT NULL,
  AlertTimestamp datetime NOT NULL,
  EscalationPeriod int(11) NOT NULL,
  Payload text,
  Priority varchar(50) DEFAULT NULL,
  PtUnitNumber varchar(50) DEFAULT NULL,
  PtName varchar(50) DEFAULT NULL,
  PtSex varchar(50) DEFAULT NULL,
  PtFMPSSN varchar(50) DEFAULT NULL,
  AlertOriginator varchar(50) DEFAULT NULL,
  Deleted int(11) DEFAULT NULL,
  UserLDAP varchar(50) DEFAULT NULL,
  PRIMARY KEY (AT_TicketID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for alerttype
-- ----------------------------
DROP TABLE IF EXISTS alerttype;
CREATE TABLE alerttype (
  AT_ID bigint(20) NOT NULL,
  AT_Name varchar(50) NOT NULL,
  AT_Description varchar(255) NOT NULL,
  PRIMARY KEY (AT_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for alertescalationcontact
-- ----------------------------
DROP TABLE IF EXISTS alertescalationcontact;
CREATE TABLE alertescalationcontact (
  AT_ContactID bigint(20) NOT NULL AUTO_INCREMENT,
  Method varchar(50) DEFAULT NULL,
  ProviderID varchar(100) DEFAULT NULL,
  ProviderName varchar(100) DEFAULT NULL,
  ProviderLDAP varchar(100) DEFAULT NULL,
  ClinicID varchar(50) DEFAULT NULL,
  OrganizationID varchar(50) DEFAULT NULL,
  Role varchar(100) DEFAULT NULL,
  Location varchar(100) DEFAULT NULL,
  AT_TicketID bigint(20) NOT NULL,
  UserID varchar(100) DEFAULT NULL,
  UserName varchar(100) DEFAULT NULL,
  UserLDAP varchar(100) DEFAULT NULL,
  PRIMARY KEY (AT_ContactID),
  KEY FK28F0F20F9C17FB6D (AT_TicketID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for alertserviceref
-- ----------------------------
DROP TABLE IF EXISTS alertserviceref;
CREATE TABLE alertserviceref (
  ASR_ID bigint(20) NOT NULL,
  ASR_Type varchar(50) NOT NULL,
  ASR_Name varchar(50) NOT NULL,
  ASR_Description varchar(255) NOT NULL,
  ASR_Location varchar(128) NOT NULL,
  ASR_Param1 varchar(128) DEFAULT NULL,
  ASR_Param2 varchar(128) DEFAULT NULL,
  ASR_Param3 varchar(128) DEFAULT NULL,
  ASR_Param4 varchar(128) DEFAULT NULL,
  PRIMARY KEY (ASR_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for at_specification
-- ----------------------------
DROP TABLE IF EXISTS at_specification;
CREATE TABLE at_specification (
  AT_SpecificationID bigint(20) NOT NULL,
  AT_ID bigint(20) NOT NULL,
  AT_AttributeName varchar(50) DEFAULT NULL,
  AT_AttributeValue varchar(255) DEFAULT NULL,
  PRIMARY KEY (AT_SpecificationID),
  KEY FK4EE949F74318740F (AT_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ecs_configuration
-- ----------------------------
DROP TABLE IF EXISTS ecs_configuration;
CREATE TABLE ecs_configuration (
  oid int(11) NOT NULL AUTO_INCREMENT,
  domainType varchar(45) NOT NULL COMMENT MEDICATIONS, ALLERGIES, ADMISSIONS, etc.,
  label varchar(80) NOT NULL COMMENT Contains the value for GUI to show.,
  sectionId int(11) DEFAULT NULL COMMENT Contains the ECS section Id.,
  activeFlag char(1) DEFAULT NULL COMMENT Y = available, N = disabled,
  modifier varchar(50) DEFAULT NULL COMMENT Contains the name of the creator/modifier,
  moddate timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT Contains the create/modified datetime stamp.,
  PRIMARY KEY (oid),
  KEY domainType (domainType)
) ENGINE=MyISAM AUTO_INCREMENT=60 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for inboxstatus
-- ----------------------------
DROP TABLE IF EXISTS inboxstatus;
CREATE TABLE inboxstatus (
  STATUS_ID bigint(20) NOT NULL,
  USER varchar(50) NOT NULL,
  SOURCE varchar(128) NOT NULL,
  ITEM varchar(128) NOT NULL,
  HAS_READ bit(1) NOT NULL,
  PRIMARY KEY (STATUS_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ref_dictionary
-- ----------------------------
DROP TABLE IF EXISTS ref_dictionary;
CREATE TABLE ref_dictionary (
  dictionary_id int(11) NOT NULL,
  concept_name varchar(255) NOT NULL,
  active_code_system_oid varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ref_fact_code
-- ----------------------------
DROP TABLE IF EXISTS ref_fact_code;
CREATE TABLE ref_fact_code (
  concept_code varchar(255) NOT NULL,
  schemetypeid int(11) NOT NULL,
  code_system_code varchar(255) DEFAULT NULL,
  code_system_name varchar(255) DEFAULT NULL,
  code_system_oid varchar(255) DEFAULT NULL,
  code_system_version varchar(255) DEFAULT NULL,
  concept_name varchar(255) DEFAULT NULL,
  hl7_table_0396_code varchar(255) DEFAULT NULL,
  preferred_alternate_code varchar(255) DEFAULT NULL,
  preferred_concept_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (concept_code,schemetypeid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ref_fact_scheme
-- ----------------------------
DROP TABLE IF EXISTS ref_fact_scheme;
CREATE TABLE ref_fact_scheme (
  factschemeid int(11) NOT NULL,
  descr varchar(255) DEFAULT NULL,
  schemename varchar(255) NOT NULL,
  PRIMARY KEY (factschemeid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ref_fact_scheme_type
-- ----------------------------
DROP TABLE IF EXISTS ref_fact_scheme_type;
CREATE TABLE ref_fact_scheme_type (
  schemetypeid int(11) NOT NULL,
  descr varchar(255) DEFAULT NULL,
  factschemeid int(11) DEFAULT NULL,
  facttypeid int(11) DEFAULT NULL,
  PRIMARY KEY (schemetypeid),
  KEY FK895E466D6D642759 (factschemeid),
  KEY FK895E466DCF1A4643 (facttypeid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ref_fact_type
-- ----------------------------
DROP TABLE IF EXISTS ref_fact_type;
CREATE TABLE ref_fact_type (
  facttypeid int(11) NOT NULL,
  facttype varchar(255) NOT NULL,
  PRIMARY KEY (facttypeid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ref_specialty_code
-- ----------------------------
DROP TABLE IF EXISTS ref_specialty_code;
CREATE TABLE ref_specialty_code (
  concept_code varchar(255) NOT NULL,
  schemetypeid int(11) NOT NULL,
  code_system_code varchar(255) DEFAULT NULL,
  code_system_name varchar(255) DEFAULT NULL,
  code_system_oid varchar(255) DEFAULT NULL,
  code_system_version varchar(255) DEFAULT NULL,
  concept_name varchar(255) DEFAULT NULL,
  hl7_table_0396_code varchar(255) DEFAULT NULL,
  preferred_alternate_code varchar(255) DEFAULT NULL,
  preferred_concept_name varchar(255) DEFAULT NULL,
  PRIMARY KEY (concept_code,schemetypeid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ref_specialty_scheme
-- ----------------------------
DROP TABLE IF EXISTS ref_specialty_scheme;
CREATE TABLE ref_specialty_scheme (
  specialtyschemeid int(11) NOT NULL,
  descr varchar(255) DEFAULT NULL,
  schemename varchar(255) NOT NULL,
  PRIMARY KEY (specialtyschemeid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for ref_taxonomy_code
-- ----------------------------
DROP TABLE IF EXISTS ref_taxonomy_code;
CREATE TABLE ref_taxonomy_code (
  concept_code varchar(255) NOT NULL,
  concept_name text NOT NULL,
  preferred_concept_name varchar(255) NOT NULL,
  preferred_alternate_code varchar(255) NOT NULL,
  code_system_oid varchar(255) NOT NULL,
  code_system_name varchar(255) NOT NULL,
  code_system_code varchar(255) NOT NULL,
  code_system_version varchar(255) NOT NULL,
  hl7_table_0396_code varchar(255) NOT NULL,
  source_filename varchar(255) NOT NULL,
  dictionary_id int(255) NOT NULL,
  disable binary(255) NOT NULL,
  KEY dictionary_oid_idx (dictionary_id,code_system_oid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for riskmodelfavorite
-- ----------------------------
DROP TABLE IF EXISTS riskmodelfavorite;
CREATE TABLE riskmodelfavorite (
  modelId varchar(45) NOT NULL,
  providerId varchar(45) NOT NULL,
  displayThreshold int(11) DEFAULT NULL,
  patientId varchar(45) DEFAULT NULL,
  riskmodelfavoriteid varchar(45) NOT NULL,
  PRIMARY KEY (riskmodelfavoriteid)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for taskserviceref
-- ----------------------------
DROP TABLE IF EXISTS taskserviceref;
CREATE TABLE taskserviceref (
  TSR_ID bigint(20) NOT NULL,
  TSR_Name varchar(50) NOT NULL,
  TSR_Description varchar(255) NOT NULL,
  TSR_Type varchar(50) NOT NULL,
  TSR_Location varchar(128) NOT NULL,
  TSR_Param1 varchar(128) DEFAULT NULL,
  TSR_Param2 varchar(128) DEFAULT NULL,
  TSR_Param3 varchar(128) DEFAULT NULL,
  TSR_Param4 varchar(128) DEFAULT NULL,
  PRIMARY KEY (TSR_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for tasktype
-- ----------------------------
DROP TABLE IF EXISTS tasktype;
CREATE TABLE tasktype (
  TT_ID bigint(20) NOT NULL,
  TT_Name varchar(50) NOT NULL,
  TT_Description varchar(255) NOT NULL,
  PRIMARY KEY (TT_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for tt_specification
-- ----------------------------
DROP TABLE IF EXISTS tt_specification;
CREATE TABLE tt_specification (
  TT_SpecificationID bigint(20) NOT NULL,
  TT_ID bigint(20) NOT NULL,
  TT_AttributeName varchar(50) DEFAULT NULL,
  TT_AttributeValue varchar(255) DEFAULT NULL,
  PRIMARY KEY (TT_SpecificationID),
  KEY FKCBCC1844916C278 (TT_ID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Table structure for xdsprocess
-- ----------------------------
DROP TABLE IF EXISTS xdsprocess;
CREATE TABLE xdsprocess (
  ProcessID bigint(20) NOT NULL,
  Ticket varchar(100) NOT NULL,
  UserId varchar(100) NOT NULL,
  PatientId varchar(100) NOT NULL,
  ResultCount bigint(20) NOT NULL,
  DownloadCount bigint(20) NOT NULL,
  ExistedCount bigint(20) NOT NULL,
  StartTime datetime NOT NULL,
  PRIMARY KEY (ProcessID)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
