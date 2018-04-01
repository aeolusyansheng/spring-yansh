SET ECHO OFF
--SPOOL DEFAULT_prof.log
/************************************************************/
/*   SqlID       : DEFAULT_PROF_prof.sql                    */
/*   SqlName     : プロファイル変更SQL                      */
/*   DBID        : pjdb                                     */
/*   UpdateDate  : 2013/11/01 13:51:25                      */
/************************************************************/
PROMPT デフォルト < DEFAULT > プロファイル変更
ALTER  PROFILE DEFAULT LIMIT
       FAILED_LOGIN_ATTEMPTS 10
       PASSWORD_LIFE_TIME UNLIMITED
       PASSWORD_LOCK_TIME UNLIMITED
       PASSWORD_GRACE_TIME UNLIMITED
;

--SPOOL OFF
EXIT
