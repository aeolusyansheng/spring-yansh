-- PJNAVI用セッション情報取得（削除）パッケージ
CREATE OR REPLACE PACKAGE XXSCM050401_PKG AS

  PROCEDURE getSessionId(
    ov_session_id      OUT NOCOPY  VARCHAR2
  );

  PROCEDURE getLoginInfo_p(
    iv_session_id      IN  VARCHAR2,
    on_result          OUT VARCHAR2,
    ov_user_id         OUT VARCHAR2,
    ov_user_name       OUT VARCHAR2,
    ov_resp_id         OUT VARCHAR2,
    ov_resp_name       OUT VARCHAR2,
    ov_appl_id         OUT VARCHAR2
  );

  PROCEDURE appsInitialize(
    in_user_id            IN VARCHAR2,
    in_responsibility_id  IN VARCHAR2,
    in_application_id     IN VARCHAR2
  );

  PROCEDURE get_request_status(
    ion_request_id    IN OUT NUMBER,
    on_status         OUT    NUMBER,
    ov_name           OUT    VARCHAR2,
    ov_phase          OUT    VARCHAR2,
    ov_status         OUT    VARCHAR2,
    ov_dev_phase      OUT    VARCHAR2,
    ov_dev_status     OUT    VARCHAR2,
    ov_message        OUT    VARCHAR2
  );

  PROCEDURE pjnavi_getLoginInfo_p(
    iv_session_id      IN  VARCHAR2,
    on_result          OUT VARCHAR2,
    ov_emp_id          OUT VARCHAR2,
    ov_user_name       OUT VARCHAR2,
    ov_resp_id         OUT VARCHAR2,
    ov_resp_name       OUT VARCHAR2,
    ov_appl_id         OUT VARCHAR2
  );

END;
/

CREATE OR REPLACE PACKAGE BODY XXSCM050401_PKG AS

  PROCEDURE getSessionId(
    ov_session_id      OUT NOCOPY  VARCHAR2
  ) IS
  BEGIN
    APPS.XXSCM050401_PKG.getSessionId@PJLK_NEXTMI(
      ov_session_id
    );
  END getSessionId;

  PROCEDURE getLoginInfo_p(
    iv_session_id      IN  VARCHAR2,
    on_result          OUT VARCHAR2,
    ov_user_id         OUT VARCHAR2,
    ov_user_name       OUT VARCHAR2,
    ov_resp_id         OUT VARCHAR2,
    ov_resp_name       OUT VARCHAR2,
    ov_appl_id         OUT VARCHAR2
  ) IS
  BEGIN
    APPS.XXSCM050401_PKG.getLoginInfo_p@PJLK_NEXTMI(
      iv_session_id,
      on_result,
      ov_user_id,
      ov_user_name,
      ov_resp_id,
      ov_resp_name,
      ov_appl_id
    );
  END getLoginInfo_p;

  PROCEDURE appsInitialize(
    in_user_id            IN VARCHAR2,
    in_responsibility_id  IN VARCHAR2,
    in_application_id     IN VARCHAR2
  ) IS
  BEGIN
    APPS.XXSCM050401_PKG.appsInitialize@PJLK_NEXTMI(
      in_user_id,
      in_responsibility_id,
      in_application_id
    );
  END appsInitialize;

  PROCEDURE get_request_status(
    ion_request_id    IN OUT NUMBER,
    on_status         OUT    NUMBER,
    ov_name           OUT    VARCHAR2,
    ov_phase          OUT    VARCHAR2,
    ov_status         OUT    VARCHAR2,
    ov_dev_phase      OUT    VARCHAR2,
    ov_dev_status     OUT    VARCHAR2,
    ov_message        OUT    VARCHAR2
  ) IS
  BEGIN
    APPS.XXSCM050401_PKG.get_request_status@PJLK_NEXTMI(
      ion_request_id,
      on_status,
      ov_name,
      ov_phase,
      ov_status,
      ov_dev_phase,
      ov_dev_status,
      ov_message
    );
  END get_request_status;

  PROCEDURE pjnavi_getLoginInfo_p(
    iv_session_id      IN  VARCHAR2,
    on_result          OUT VARCHAR2,
    ov_emp_id          OUT VARCHAR2,
    ov_user_name       OUT VARCHAR2,
    ov_resp_id         OUT VARCHAR2,
    ov_resp_name       OUT VARCHAR2,
    ov_appl_id         OUT VARCHAR2
  ) IS
  BEGIN
    APPS.XXSCM050401_PKG.pjnavi_getLoginInfo_p@PJLK_NEXTMI(
      iv_session_id,
      on_result,
      ov_emp_id,
      ov_user_name,
      ov_resp_id,
      ov_resp_name,
      ov_appl_id
    );
  END pjnavi_getLoginInfo_p;

END XXSCM050401_PKG;
/
