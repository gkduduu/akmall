

#define MSG_NORMAL(TITLE, FRIST_BUTTON_TITLE) [[[[UIAlertView alloc] initWithTitle:TITLE message:nil delegate:nil cancelButtonTitle:FRIST_BUTTON_TITLE otherButtonTitles:nil, nil] autorelease] show]


#define MSG(TITLE, FRIST_BUTTON_TITLE, MSG) [[[[UIAlertView alloc] initWithTitle:TITLE message:MSG delegate:nil cancelButtonTitle:FRIST_BUTTON_TITLE otherButtonTitles:nil, nil] autorelease] show]

#define MSG_DELEGATE(TITLE, FRIST_BUTTON_TITLE, MSG, DELEGATE) [[[[UIAlertView alloc] initWithTitle:TITLE message:MSG delegate:DELEGATE cancelButtonTitle:FRIST_BUTTON_TITLE otherButtonTitles:nil, nil] autorelease] show]

#define MSG_DELEGATE_BTN2(TITLE, CANCEL_BUTTON_TITLE, OK_BUTTON_TITLE, MSG, DELEGATE) [[[[UIAlertView alloc] initWithTitle:TITLE message:MSG delegate:DELEGATE cancelButtonTitle:CANCEL_BUTTON_TITLE otherButtonTitles:OK_BUTTON_TITLE, nil] autorelease] show]


#define showAlert(format, ...) myShowAlert(__LINE__, (char *)__FUNCTION__, format, ##__VA_ARGS__)