DROP TABLE `kmr`.`factlabelconfig`;
CREATE TABLE `factlabelconfig` (
  `oid` int(11) NOT NULL auto_increment,
  `domainType` varchar(45) NOT NULL COMMENT 'MEDICATIONS, ALLERGIES, ADMISSIONS, etc.',
  `label` varchar(80) NOT NULL COMMENT 'Contains the value for GUI to show.',
  `sectionId` int(11) default NULL COMMENT 'Contains the ECS section Id.',
  `activeFlag` char(1) default NULL COMMENT 'Y = available, N = disabled',
  `modifier` varchar(50) default NULL COMMENT 'Contains the name of the creator/modifier',
  `moddate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Contains the create/modified datetime stamp.',
  PRIMARY KEY  (`oid`),
  KEY  (`domainType`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

INSERT INTO 
`factlabelconfig`(`domainType`,`label`,`sectionId`,`activeFlag`,`modifier`) 
VALUES 

 ('ADMISSIONS'      ,'Detail'                 ,0    ,'Y', 'tmn')

,('ALLERGIES'       ,'Reactions'              ,0    ,'Y', 'tmn')

,('DEMOGRAPHICS'    ,'Address'                ,0    ,'Y', 'tmn')
,('DEMOGRAPHICS'    ,'Telcom'                 ,0    ,'Y', 'tmn')
,('DEMOGRAPHICS'    ,'Accounts'               ,0    ,'Y', 'tmn')

,('DOC_OUTPATIENT'  ,'Detail'                 ,0    ,'Y', 'tmn') 
,('DOC_OUTPATIENT'  ,'Summary Information'    ,2    ,'Y', 'tmn')
,('DOC_INPATIENT'   ,'Detail'                 ,0    ,'Y', 'tmn')

,('IMMUNIZATIONS'   ,'Detail'                 ,0    ,'Y', 'tmn')
,('IMMUNIZATIONS'   ,'Background'             ,101  ,'Y', 'tmn')
,('IMMUNIZATIONS'   ,'Disease'                ,103  ,'Y', 'tmn')
,('IMMUNIZATIONS'   ,'Podcasts'               ,22   ,'Y', 'tmn')
,('IMMUNIZATIONS'   ,'Videos'                 ,21   ,'Y', 'tmn')

,('MEDICATIONS'     ,'Dispensations'          ,0    ,'Y', 'tmn')
,('MEDICATIONS'     ,'Description'            ,4    ,'Y', 'tmn')
,('MEDICATIONS'     ,'Instructions'           ,102  ,'Y', 'tmn')
,('MEDICATIONS'     ,'Indications'            ,104  ,'Y', 'tmn')
,('MEDICATIONS'     ,'Side-Effects'           ,105  ,'Y', 'tmn')
,('MEDICATIONS'     ,'Potential Interactions' ,106  ,'Y', 'tmn')
,('MEDICATIONS'     ,'Additional References'  ,5    ,'N', 'tmn')

,('PROBLEMS'        ,'Detail'                 ,0    ,'Y', 'tmn')
,('PROBLEMS'        ,'Summary Information'    ,2    ,'Y', 'tmn')
,('PROBLEMS'        ,'Diagnoses'              ,107  ,'Y', 'tmn')
,('PROBLEMS'        ,'Treatment'              ,108  ,'Y', 'tmn')
,('PROBLEMS'        ,'Literature'             ,23   ,'Y', 'tmn')
,('PROBLEMS'        ,'Podcasts'               ,22   ,'Y', 'tmn')
,('PROBLEMS'        ,'Videos'                 ,21   ,'Y', 'tmn')

,('IMAGING'  ,'Detail'                 ,0    ,'Y', 'tmn')
,('IMAGING'  ,'Images'                 ,20   ,'Y', 'tmn')
,('IMAGING'  ,'Summary Information'    ,2    ,'Y', 'tmn')
,('IMAGING'  ,'Indications'            ,104  ,'Y', 'tmn')
,('IMAGING'  ,'Risk-Benefit'           ,109  ,'Y', 'tmn')
,('IMAGING'  ,'Literature'             ,23   ,'Y', 'tmn')
,('IMAGING'  ,'Podcasts'               ,22   ,'Y', 'tmn')
,('IMAGING'  ,'Videos'                 ,21   ,'Y', 'tmn')


,('PROCEDURES'  ,'Detail'              ,0     ,'Y', 'tmn')
,('PROCEDURES'  ,'Images'              ,20    ,'Y', 'tmn')
,('PROCEDURES'  ,'Summary Information' ,2     ,'Y', 'tmn')
,('PROCEDURES'  ,'Indications'         ,104   ,'Y', 'tmn')
,('PROCEDURES'  ,'Risk-Benefit'        ,109   ,'Y', 'tmn')
,('PROCEDURES'  ,'Literature'          ,23    ,'Y', 'tmn')
,('PROCEDURES'  ,'Podcasts'            ,22    ,'Y', 'tmn')
,('PROCEDURES'  ,'Videos'              ,21    ,'Y', 'tmn')

,('LABS'  ,'Results'             ,0     ,'Y', 'tmn')
,('LABS'  ,'Description'         ,4     ,'Y', 'tmn')
,('LABS'  ,'Images'              ,20    ,'Y', 'tmn')
,('LABS'  ,'Summary Information' ,2     ,'Y', 'tmn')
,('LABS'  ,'Indications'         ,104   ,'Y', 'tmn')
,('LABS'  ,'Risk-Benefit'        ,109   ,'Y', 'tmn')
,('LABS'  ,'Literature'          ,23    ,'Y', 'tmn')
,('LABS'  ,'Podcasts'            ,22    ,'Y', 'tmn')
,('LABS'  ,'Videos'              ,21    ,'Y', 'tmn')

;
COMMIT;



