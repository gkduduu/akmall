//
//  LoginViewController.m
//  testAKMall06
//
//  Created by uniwis on 11. 5. 6..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "LoginViewController.h"

#import "UIUnderlineLabel.h"
#import "LoginMsg.h"

/*
#pragma mark
#pragma mark making the SSL Allowed..
@interface NSURLRequest (DummyInterface)
+(BOOL)allowsAnyHTTPSCertificateForHost:(NSString *)host;
+(void)setAllowsAnyHTTPSCertificate:(BOOL)allow forHost:(NSString *)host;
@end
*/

@implementation LoginViewController
@synthesize txtUserID;
@synthesize txtUserPassword;
@synthesize btnLogin;
@synthesize activityView;
@synthesize queryData, queryString, urlCon, rawData, dicData;
@synthesize isLeftBtn;

- (void)dealloc
{
    
    
    [btnLogin release];
    [txtUserID release];
    [txtUserPassword release];
    [activityView release];
    [queryData release];
    [queryString release];
    [urlCon release];
    [rawData release];
    [dicData release];
    
    
    [super dealloc];
}


- (void)viewDidUnload
{
    [self setActivityView:nil];
    [self setBtnLogin:nil];
    [self setTxtUserID:nil];
    [self setTxtUserPassword:nil];
    

    
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


- (IBAction)onBtnBackground:(id)sender {
    NSArray *subs = self.contentView.subviews;
    
    UIView* curView;
    UIView* cView;
    
    for (curView in subs) {
        
        for (cView in curView.subviews) {
            
            if ([cView conformsToProtocol:@protocol(UITextInputTraits)]) 
            {
                [cView resignFirstResponder];
            }
        }
    }
}

-(void)onCheckBox:(id)sender{
    //이벤트 발생버튼 지정
	UIButton *button = sender;
    //선택값을 반전 시켜줌
	button.selected=!button.selected;
    
    [button release];
}

- (IBAction)procLogin:(id)sender {
    
    
    OLogin *oLogin = [[OLogin alloc ] init];
    
    oLogin.isAutoLogin = isAutoLogin;
    oLogin.userID = self.txtUserID.text;
    oLogin.userPW = self.txtUserPassword.text;
    
    
    AkLoginModel *oLoginModel = [[AkLoginModel alloc] init];
    
    oLoginModel.delegate = self;
    
    [oLoginModel procLogin:oLogin];

    
}

#pragma mark - baseModel

-(UINavigationController *)navigationControllerSetting
{
    return self.navigationController;
}

-(void)activityStartView:(UIActivityIndicatorView *)act
//-(void)activityStartView:(MBProgressHUD *)act
{
    //로딩 화면 출력될 때 ActivityIndicatorView 보이기
	self.activityView = act;
    
    //changuk 2013.08.26 정중앙으로 변경
//	self.activityView.center = CGPointMake(160, 169);
    self.activityView.center = self.contentView.center;
	
    [self.contentView insertSubview:self.activityView atIndex:999];
    
    [self.activityView startAnimating];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    //popViewControllerAnimated - 현재 뷰 컨트롤러를 제거하고 이전 존재하던 뷰 컨트롤러가 전면에 나타난다. 

    if (buttonIndex == 0) {
        [self.navigationController popViewControllerAnimated:YES];
    }

   
    

}



-(void)onAutoLoginCheckBox:(id)sender{
    //이벤트 발생버튼 지정
	UIButton *button = sender;
    //선택값을 반전 시켜줌
	button.selected=!button.selected;
    
    if (button.selected) {
        isAutoLogin = YES;
    }  
    else
    {
        isAutoLogin = NO;
    }
    
    
}

//체크박스 사용방법.. 
-(void)viewAutoLoginCheckBox
{
    //버튼을 커스텀으로 작성
    UIButton *checkbox = [UIButton buttonWithType:UIButtonTypeCustom];
    
    if ([[NSUserDefaults standardUserDefaults] boolForKey: @"isAutoLogin"]) 
    {
        isAutoLogin = YES;
        checkbox.selected = YES;
    }
    else
    {
        isAutoLogin = NO;
        checkbox.selected = NO;
    }
    
    checkbox.tag = 88;
    
    UIImage *imgCheckbox_off = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"login_aout_off" ofType:@"png"]];
    
    UIImage *imgCheckbox_on = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"login_aout_on" ofType:@"png"]];
    
    //위치 및 크기 조절
    checkbox.frame = CGRectMake(121, 129, 77.5, 15);
    //바탕색상조절
    //checkbox.backgroundColor = [UIColor blueColor];
    
    
    //노말상태에서 타이틀지정
    //[checkbox setTitle:@"OFF" forState:UIControlStateNormal];
    [checkbox setImage:imgCheckbox_off forState:UIControlStateNormal];
    //선택된상태에서 타이틀지정
    //[checkbox setTitle:@"ON" forState:UIControlStateSelected];
    [checkbox setImage:imgCheckbox_on forState:UIControlStateSelected];
    //클릭시 이벤트 지정
    [checkbox addTarget:self action:@selector(onAutoLoginCheckBox:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.contentView addSubview:checkbox];
    
    
    
    
}





- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}



- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle
- (void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
    
    
    if (self.isLeftBtn) {
//        [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
//        [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];

    }
    
    


}


-(void)doLeftButton:(id)sender
{
    
    if (self.isLeftBtn) 
    {
        [self.navigationController popViewControllerAnimated:YES];
        
        
        NSString *pushid = [[NSUserDefaults standardUserDefaults] objectForKey:@"PID"];
        DLog(@"pushid : %@", pushid);
        
        if (pushid && [pushid length] > 0) 
        {
        
            //각 지점별 알림 처리 후 ...
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"PID"];
            [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"BC"];
            
            //알럿 메세지 처리 
            MSG(nil, @"확인", @"해당 알림 메시지를 확인 하려면 로그인을 해야합니다");
            
            
        }
        
        pushid = nil;
    }
    
    
    
}

-(void)clickBtnTextFieldHidden
{
    
}



-(void)procJoin
{
    
    
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@?act=signup&isAkApp=iPhone&appVersion=%@", kAddressURL, [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"] ]]];
 
    
    
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    
    
    
    UIImageView *ImgLoginLogo = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"login_box" ofType:@"png"]]];
    ImgLoginLogo.frame = CGRectMake(45, 25, 230, 89);
    
    ImgLoginLogo.userInteractionEnabled = YES;
    
    [self.contentView addSubview:ImgLoginLogo];
    
    
    
    
    /*
    
    UIImageView *ImgLoginBox = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"login_box" ofType:@"png"]]];
    ImgLoginBox.frame = CGRectMake(54, 55, 211, 71);
    ImgLoginBox.userInteractionEnabled = YES;

    
    */
    
    
    btnTextFieldHidden = [UIButton buttonWithType:UIButtonTypeCustom];
    btnTextFieldHidden.frame = CGRectMake(0, 0, 320, 400);
    [btnTextFieldHidden addTarget:self action:@selector(onBtnBackground:) forControlEvents:UIControlEventTouchUpInside];
    btnTextFieldHidden.userInteractionEnabled   = YES;

    [self.contentView addSubview:btnTextFieldHidden ];
    
     
    
    btnLogin = [UIButton buttonWithType:UIButtonTypeCustom];
    [btnLogin setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"login_btn" ofType:@"png"]] forState:UIControlStateNormal];
    [btnLogin setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"login_btn_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    btnLogin.frame = CGRectMake(45, 159, 230, 35);
    //btnLogin.titleLabel.text = @"로그인";
    //btnLogin.titleLabel.textColor = [UIColor blackColor];
    
    [btnLogin addTarget:self action:@selector(procLogin:) forControlEvents:UIControlEventTouchUpInside];
    
    /* 버튼 텍스트명 설정 */
    //[btnLogin setTitle:@"로그인"                forState:UIControlStateNormal];
    //[btnLogin setTitle:@"로그인"                forState:UIControlStateSelected];
    
    [self.contentView addSubview:btnLogin];
    


    txtUserID = [[UITextField alloc] initWithFrame:CGRectMake(5, 0, 220, 44)];
    txtUserID.font = kFontSizeLarge;
    txtUserID.keyboardType = UIKeyboardTypeAlphabet;
    txtUserID.delegate = self;    
    txtUserID.textColor = kFontBrownColor;
    [txtUserID setPlaceholder:@"아이디"];
    
    
    
    txtUserPassword = [[UITextField alloc] initWithFrame:CGRectMake(5, 45, 220, 44)];
    txtUserPassword.font =kFontSizeLarge;
    txtUserPassword.keyboardType = UIKeyboardTypeAlphabet;
    txtUserPassword.delegate = self;
    txtUserPassword.textColor = kFontBrownColor;
    [txtUserPassword setPlaceholder:@"비밀번호"];
    [txtUserPassword setSecureTextEntry:YES];
    
    
    [ImgLoginLogo addSubview:txtUserID];
    [ImgLoginLogo addSubview:txtUserPassword];
    
    [self.contentView addSubview:ImgLoginLogo];
    
    
    [self viewAutoLoginCheckBox];
    // Do any additional setup after loading the view from its nib.
    
    [ImgLoginLogo release];
    
    //[btnTextFieldHidden release];
    
    //MAKCore/login.do?act=login&Id=myid55&Pass=&type=App

    
    

    
    UIView *infoView = [[UIView alloc] initWithFrame: CGRectMake(0, 219.5, 320, 143.5)];
    infoView.backgroundColor = kLoginInfoBgColor;

    
    
    //회원가입 버튼 //////////////////////////////////////////////////////////////////////////////////////////
    
    UIButton *btnJoin = [UIButton buttonWithType:UIButtonTypeCustom];
    btnJoin.frame = CGRectMake(125, 10, 70, 20);
    
 
    
    
    //로그인이 성공하면 로그아웃 버튼으로 변경하기 
    [btnJoin addTarget:self action:@selector(procJoin) forControlEvents:UIControlEventTouchUpInside];
    
    
    [infoView addSubview:btnJoin];
    
    
    
    ///문구 /////////////////////////////////////////////
    
    UILabel * lblJoinInfo = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 70, 15)];
    [lblJoinInfo setFont:[UIFont systemFontOfSize:12.0]];
    lblJoinInfo.text = @"[회원가입]";
    
    lblJoinInfo.numberOfLines = 1;
    lblJoinInfo.textAlignment = UITextAlignmentLeft;
    lblJoinInfo.backgroundColor = [UIColor clearColor];
    lblJoinInfo.textColor = kFontWeightiernColor;
    
    [btnJoin addSubview:lblJoinInfo];  
    
    
    UIImageView *textBuletImg1 = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
    
    textBuletImg1.frame = CGRectMake(45, 11, 6.5, 16);
    
    [infoView addSubview:textBuletImg1];
    
    UILabel *lblInfo2 = [[UILabel alloc] initWithFrame:CGRectMake(51.5, 10, 230, 32)];
    lblInfo2.backgroundColor = [UIColor clearColor];
    lblInfo2.text = @"AK Members                   하고, 다양한 혜택과 서비스를 이용하세요.";
    lblInfo2.textColor = kFontBrownColor;
    lblInfo2.numberOfLines = 2;
    lblInfo2.textAlignment = UITextAlignmentLeft;
    lblInfo2.font = kFontSizeDefault;
    
    [infoView addSubview:lblInfo2];   
    
    
    UIImageView *textBuletImg2 = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
    
    textBuletImg2.frame = CGRectMake(45, 47, 6.5, 16);
    
    [infoView addSubview:textBuletImg2];
    
    
    
    UILabel *lblInfo3 = [[UILabel alloc] initWithFrame:CGRectMake(51.5, 47, 230, 32)];
    lblInfo3.backgroundColor = [UIColor clearColor];
    lblInfo3.text = @"[아이디/비밀번호] 찾기는 PC버전에서 가능합니다";
    lblInfo3.textColor = kFontBrownColor;
    lblInfo3.numberOfLines = 2;
    lblInfo3.textAlignment = UITextAlignmentLeft;
    lblInfo3.font = kFontSizeDefault;
    
    [infoView addSubview:lblInfo3];   
    
    
    [self.contentView addSubview:infoView];

}



- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
#pragma mark - UITextFieldDelegate
-(BOOL)textFieldShouldReturn:(UITextField *)textField
{
    
    
    
    [self performSelectorOnMainThread:@selector(procLogin:) withObject:nil waitUntilDone:YES];
    [textField resignFirstResponder];
    
    return YES;
}





@end
