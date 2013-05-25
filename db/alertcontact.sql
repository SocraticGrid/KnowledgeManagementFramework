

truncate table alertticket;
truncate table alertcontact;
truncate table alertstatus;
truncate table alertaction;


CREATE TABLE `alertcontact` (
  `AT_ContactID` bigint(20) NOT NULL AUTO_INCREMENT,
  `Method` varchar(50) DEFAULT NULL,
  `UserID` varchar(100) DEFAULT NULL,
  `UserName` varchar(100) DEFAULT NULL,
  `UserLDAP` varchar(100) DEFAULT NULL,
  `ClinicID` varchar(50) DEFAULT NULL,
  `OrganizationID` varchar(50) DEFAULT NULL,
  `Role` varchar(100) DEFAULT NULL,
  `Location` varchar(100) DEFAULT NULL,
  `AT_TicketID` bigint(20) NOT NULL,
  PRIMARY KEY (`AT_ContactID`),
  KEY `FK6CFD9B449C17FB6D` (`AT_TicketID`)
) ENGINE=MyISAM AUTO_INCREMENT=52 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;


drop table `alertaction`;
CREATE TABLE `alertaction` (
  `AA_ActionID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ProviderID` varchar(100) DEFAULT NULL,
  `ProviderName` varchar(100) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Message` varchar(255) DEFAULT NULL,
  `Timestamp` datetime NOT NULL,
  `AT_TicketID` bigint(20) NOT NULL,
  `UserID` varchar(100) DEFAULT NULL,
  `UserName` varchar(100) DEFAULT NULL,
  `UserProvider` bit(1) DEFAULT NULL,
  PRIMARY KEY (`AA_ActionID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

drop table `alertstatus`;
CREATE TABLE `alertstatus` (
  `AT_StatusID` bigint(20) NOT NULL AUTO_INCREMENT,
  `UserId` varchar(50) DEFAULT NULL,
  `CreateTimeStamp` datetime NOT NULL,
  `Flagged` bit(1) DEFAULT b'0',
  `Archive` bit(1) DEFAULT b'0',
  `Deleted` bit(1) DEFAULT b'0',
  `AT_TicketID` bigint(20) NOT NULL,
  PRIMARY KEY (`AT_StatusID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

