//
//  NoticeViewController.m
//  UWCutomView
//
//  Created by uniwis on 11. 6. 8..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "AkLoginWithNotiSettingView.h"
#import "LoginViewController.h"
#import "snsKey.h"
#import "AkNotiModel.h"
#import "GlobalValues.h"

#define kAccessToken @"accessToken"

#define kFacebookLogoutTitle @"페이스북 로그아웃"
#define kTwittLogoutTitle @"트위터 로그아웃"
#define kPlazaLogoutTitle @"로그아웃"

@implementation AkLoginWithNotiSettingView
@synthesize tableView;
@synthesize accessToken;
@synthesize loginDialogView;
@synthesize oAuth;
@synthesize twitter, fb;
@synthesize activityView;


- (void)dealloc
{
    [tableView release];
    
    [oAuth release];
    [twitter release];
    [loginDialogView release];
    [accessToken release];
    [fb release];
    [activityView release];
    
    [super dealloc];
}



- (void)viewDidUnload
{
    
    self.accessToken = nil;
    self.loginDialogView = nil;
    self.oAuth = nil;
    self.twitter = nil;
    self.fb = nil;
    self.tableView = nil;
    self.activityView = nil;
    
    
    
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}




- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle
-(void)loadView
{
    self.view = nil;
    
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
    
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
}




-(void)doLeftButton:(id)sender
{
   [self.navigationController popViewControllerAnimated:YES]; 
}



// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    

    UIView *titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 35)];
    titleView.backgroundColor = kTitleBgColorBrown;
    
    CALayer *bottom = [CALayer layer];
    bottom.backgroundColor = kTitleBorderColor.CGColor;
    bottom.frame = CGRectMake(0, titleView.frame.size.height-kTitleBorderWidth, titleView.frame.size.width, kTitleBorderWidth);
    [titleView.layer addSublayer:bottom];
    
    
    UIImageView *titleBuletImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"title_bulet" ofType:@"png"]]];
    
    titleBuletImg.frame = CGRectMake(10, 9, 8, 17);
    
    [titleView addSubview:titleBuletImg];
    
    
    
    
    UILabel *lblTitle = [[UILabel alloc] initWithFrame:CGRectMake(19, 9, 291, 17)];
    lblTitle.backgroundColor = [UIColor clearColor];
    lblTitle.text = @"사용자 설정";
    lblTitle.textColor = kFontTitleColor;
    lblTitle.textAlignment = UITextAlignmentLeft;
    lblTitle.font = kFontSizeTitle;
    
    [titleView addSubview:lblTitle];
    
    [self.contentView addSubview:titleView];
    
    
    [titleView release];
    titleView = nil;
    [titleBuletImg release];
    titleBuletImg = nil;
    [lblTitle release];
    lblTitle = nil; 
    
    
    /*
    UIImageView *ImgNoticeBg = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"notice_bg.png"]];
    
    ImgNoticeBg.frame = CGRectMake(0, 50, 320, 360);
    
    [self.view addSubview:ImgNoticeBg];
    
    [ImgNoticeBg release];
    
    */
    
    self.tableView = nil;
    CGRect tableViewFrame = CGRectMake(0.0, 35, self.view.bounds.size.width, self.view.bounds.size.height);
    self.tableView = [[UITableView alloc] initWithFrame:tableViewFrame style:UITableViewStyleGrouped];
    self.tableView.autoresizingMask = (UIViewAutoresizingFlexibleWidth|UIViewAutoresizingFlexibleHeight);
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    self.tableView.backgroundColor = kBgColor;
    
    [self.contentView addSubview:self.tableView];
    [self.tableView reloadData];

    
}

- (void)viewWillAppear:(BOOL)animated
{
    
    
    [self.tableView reloadData];
    
}





- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma tableview
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    switch (section) {
        case 0:
            return 3;
            break;
            
        case 1:
            return 1;
            break;
    }
    
    
    return 0;
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    switch (indexPath.section) {
        case 0:
            
            switch (indexPath.row) {
                case 0:
                    
                    return 44;
                    break;
                    
                case 1:
                    return 44;
                    break;
                    
                case 2:
                    return 45;
                    break;
            }

            break;
            
        case 1:
            return 45;
            break;
    }
    
    
    return 0;
}


//-(NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
//{
//    switch (section) {
//        case 0 :
//            return @"로그인 설정";
//            break;
//            
//        case 1 :
//            return @"알리미 설정";
//            break;
//    }
//    
//    return @"";
//}

//- (NSString *)tableView:(UITableView *)tableView titleForFooterInSection:(NSInteger)section {
//    if(section == 1)
//		return @"알림 기능을 설정하시면 AK PLAZA 이벤트 및 쇼핑혜택 정보를 받으실 수 있습니다";
//    else
//		return nil;
//}

-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    UIImage *image = nil;
    
    if (indexPath.section == 0) {
        
        
        if (indexPath.row == 0) {
            
            image = [UIImage imageNamed:@"setting_box_top.png"];
            //[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"setting_box_top" ofType:@"png"]];
            
        }
        else if (indexPath.row == 1)
        {
            image = [UIImage imageNamed:@"setting_box_middle.png"];
            //[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"setting_box_middle" ofType:@"png"]];
            
        }
        else if (indexPath.row == 2)
        {
            image = [UIImage imageNamed:@"setting_box_bottom.png"];
            //[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"setting_box_bottom" ofType:@"png"]];
            
        }
        

        
    }
    /*
    else
    {
        image = [UIImage imageNamed:@"setting_box.png"];
        //[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"setting_box" ofType:@"png"]];
    }
    */
    
    
    UIImageView *imgView = [[UIImageView alloc] initWithImage:image];
    
    cell.backgroundView = imgView;
    cell.selectionStyle = UITableViewCellSelectionStyleNone;

    cell.detailTextLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    
    //cell.selectedBackgroundView = imgView;
    
    [imgView release];
    
    imgView = nil;
    image = nil;
    
}

/*
-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    if(section == 1)
        return 60;
    else
        return 10;
}


-(UIView *)tableView:(UITableView *)tableView viewForFooterInSection:(NSInteger)section
{
    if(section == 1)
    {
        
        //UIImageView *textBuletImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
        
        //textBuletImg.frame = CGRectMake(7, 17, 11, 11);
        NSString *strPushState = [[UIApplication sharedApplication] enabledRemoteNotificationTypes] ? @"알림 켜짐" : @"알림 꺼짐";
        
        DLog(@"알림 상태 : %@", strPushState);
        
        strPushState = nil;
        
        
        UILabel * lblTitle1 = nil;
        
        lblTitle1 = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 310, 60)];
        lblTitle1.font = kFontSizeDefault;
        lblTitle1.text = @"알림 기능을 설정하시면 AK PLAZA 이벤트 및\n쇼핑혜택 정보를 받으실 수 있습니다";
        lblTitle1.numberOfLines = 3;
        lblTitle1.backgroundColor = [UIColor clearColor];
        lblTitle1.textAlignment = UITextAlignmentCenter;
        lblTitle1.textColor = kFontBrownColor;
        
        UIView *view = nil;
        
        view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 60)];
        
        //[view addSubview:textBuletImg];
        [view addSubview:lblTitle1];
        
        [lblTitle1 release];
        lblTitle1 = nil;
        
        return [view autorelease];
    }
    
    
    return nil;
}

 */

- (void)switchAction:(id)sender
{
    UISwitch *sw = sender;
    
    NSString * token =[[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    
    if (token != nil && [token length] > 0) {
        
        //설정 업데이트 
         
        AkNotiModel *oNotiModel = [[AkNotiModel alloc] init];
        
        oNotiModel.delegate = self;
        
        //deny :  0 - off, 1 - on   (알림사용 여부)
        NSString *deny = sw.isOn ? @"1" : @"0";
        
        
        if ([deny isEqualToString:@"0"]) {
            [[NSUserDefaults standardUserDefaults] setBool:NO forKey:@"PUSHSET"];
        }
        else
        {
            [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"PUSHSET"];
        }
        
       
        [oNotiModel performSelector:@selector(notiSetUpdate:) withObject:deny];
        
        [oNotiModel release];
        oNotiModel = nil;

    }
    else
    {
        
        //최초 등록
        
        //if (sw.isOn) {
            //2012.04.08 myak하면서 로직이 바뀜 
            //getDenyState로 서버단에 알림설정정보 받아서 처리하기 

            //알림설정할때 푸시 등록하기 
            //[[UIApplication sharedApplication] registerForRemoteNotificationTypes:UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeBadge | UIRemoteNotificationTypeSound];
            
            
        //}

    }
    
    
    
    
    
    
}

- (UITableViewCell *)tableView:(UITableView *)table cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [table dequeueReusableCellWithIdentifier:@"LoginSetCell"];
    
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"LoginSetCell"] autorelease];
    }
    
    if (indexPath.section == 0) {
        if (indexPath.row == 0) {
            cell.textLabel.text = @"AK Plaza 로그인";
            
            
            OLogin* oLogin = [OLogin loadOLoginContextFromUserDefaults];
            
            if (oLogin.isLogin) 
            {
                cell.detailTextLabel.text = oLogin.userID;
                
                oLogin = nil;
            }
            else
            {
                cell.detailTextLabel.text = @"";
            }
            
            
            
            
        }
        else if (indexPath.row == 1) {
            
            OAuth* oauth = [[OAuth alloc] initWithConsumerKey:OAUTH_CONSUMER_KEY andConsumerSecret:OAUTH_CONSUMER_SECRET];
            [oauth loadOAuthTwitterContextFromUserDefaults];
            
            
            
            cell.textLabel.text = @"트위터 로그인";
            cell.detailTextLabel.text = oauth.oauth_token_authorized ? oauth.screen_name : @"미설정" ;
            
            [oauth release];
            oauth = nil;
            
        }
        else
        {
            self.accessToken = nil;
            self.accessToken = [[NSUserDefaults standardUserDefaults] stringForKey:kAccessToken];
            
            cell.textLabel.text = @"페이스북 로그인";
            cell.detailTextLabel.text = self.accessToken != nil ? @"설정됨" : @"미설정" ;
            

        }
        
        
        cell.detailTextLabel.font = kFontSizeSub;

        
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        
        //액세서리 뷰 이미지 바꾸기~ 
        UIImage* image = [UIImage imageNamed:@"arrow.png"];
        //[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow" ofType:@"png"]];
        
        UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
        
        cell.accessoryView = imgView;
        
        [imgView release];
        
        imgView = nil;
        image = nil;
    }
    /*
    else
    {
        
        cell.textLabel.text = @"알림 설정";
        
        UISwitch *notiSwitch = [[[UISwitch alloc] init ] autorelease];
        
        AkNotiModel *oNotiModel = [[AkNotiModel alloc] init];
        
        //oNotiModel.delegate = self;
        
        [oNotiModel performSelector:@selector(notiSetState) withObject:nil];
        
        [oNotiModel release];
        oNotiModel = nil;
        
        BOOL isPushSet = [[NSUserDefaults standardUserDefaults] boolForKey:@"PUSHSET"];
        
        if (isPushSet) {
            [notiSwitch setOn:YES animated:NO];
        }
        else
        {
            [notiSwitch setOn:NO animated:NO];
        }
        
        
        
        [notiSwitch addTarget:self action:@selector(switchAction:) forControlEvents:UIControlEventTouchUpInside];
        
        
        //CustomUISwitch *notiSwitch = [[[CustomUISwitch alloc ]init]autorelease];
        
        //[notiSwitch setOn:NO animated:NO];
        
        //[notiSwitch addTarget:self action:@selector(switchAction:) forControlEvents:UIControlEventTouchUpInside];

         
        
        cell.accessoryView = notiSwitch;
        
        notiSwitch = nil;
        
        
    }
    */
    
    
    cell.textLabel.font = kFontSizeLarge;

    cell.textLabel.backgroundColor = [UIColor clearColor];
    cell.detailTextLabel.backgroundColor = [UIColor clearColor];
    cell.textLabel.textColor = kFontBrownColor;
    cell.detailTextLabel.textColor = kFontGrayColor;
    
    
    return cell;
}   

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0) {
        if (indexPath.row == 0) {
            
            OLogin* oLogin = [OLogin loadOLoginContextFromUserDefaults];
            
            if (oLogin.isLogin) 
            {
                MSG_DELEGATE_BTN2(@"로그아웃", @"취소", @"확인", @"로그아웃 하시겠습니까?", self);
                
                oLogin = nil;
            }
            else
            {
                [[[GlobalValues sharedSingleton] tabBar].webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/auth.do?act=login",kRootURL]]]];
                [self.navigationController popViewControllerAnimated:YES];
//                LoginViewController *loginView = [[LoginViewController alloc] init];
//                
//                loginView.isLeftBtn = YES;
//                
//                [self.navigationController pushViewController:loginView animated:YES];
//                
//                loginView = nil;
            }
                
            
        }
        else
        {
            self.fb = nil;
            self.twitter = nil;
            
            if (indexPath.row == 1) {
                //[self.navigationController pushViewController:[[AkLoginSetViewController alloc] init] animated:YES];
                //MSG_NORMAL(@"트위터 설정 기능 넣어야 함", @"확인");
                
                self.oAuth = [[OAuth alloc] initWithConsumerKey:OAUTH_CONSUMER_KEY andConsumerSecret:OAUTH_CONSUMER_SECRET];
                
                
                [self.oAuth loadOAuthTwitterContextFromUserDefaults];
                
                if (self.oAuth.oauth_token_authorized) 
                {
                    //MSG_NORMAL(@"이미 설정되어있습니다.", @"확인");
                    
                    MSG_DELEGATE_BTN2(kTwittLogoutTitle, @"취소", @"확인", @"로그아웃 하시겠습니까?", self);
                }
                else
                {
                    
                    self.twitter = [[CustomLoginPopup alloc] initWithNibName:@"TwitterLoginPopup" bundle:nil];  
                    
                    self.twitter.oAuth = oAuth;
                    self.twitter.delegate = self;
                    self.twitter.uiDelegate = self;
                    
                    
                    [self presentModalViewController:self.twitter animated:YES];           
                }
                
                
            }
            else
            {
                //[self.navigationController pushViewController:[[AkLoginSetViewController alloc] init] animated:YES];
                //MSG_NORMAL(@"페이스북 설정 기능 넣어야 함", @"확인");
                
                self.accessToken = [[NSUserDefaults standardUserDefaults] stringForKey:kAccessToken];
                
                if (self.accessToken == nil) 
                {
                    
                    
                    //쓰기 권한 : publish_stream
                    //로그인 유지 권한 : offline_access
                    NSString *requestedPermissions = @"publish_stream,offline_access";
                    
                    
                    self.fb = [[FBFunLoginDialog alloc] initWithAppId:kFacebook_APIKey requestedPermissions:requestedPermissions delegate:self] ;
                    
                    [self presentModalViewController:self.fb animated:YES];
                    
                }
                else
                {
                    //MSG_NORMAL(@"이미 설정되어있습니다.", @"확인");
                    
                    //로그아웃
                    
                    MSG_DELEGATE_BTN2(kFacebookLogoutTitle, @"취소", @"확인", @"로그아웃 하시겠습니까?", self);
                }
            }
        }
            
    }
    else
    {
        
    }
}






-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if ([alertView.title isEqualToString:kPlazaLogoutTitle]) {
        if (buttonIndex == 0) 
        {
            //취소
        }
        else
        {
            AkLoginModel *oLoginModel = [[AkLoginModel alloc] init];

            oLoginModel.delegate = self;

            [oLoginModel performSelectorOnMainThread:@selector(procLogout) withObject:nil waitUntilDone:YES];

            OLogin *oLogin = [OLogin loadOLoginContextFromUserDefaults];

            if(!oLogin.isLogin)
            {
                [self.tableView reloadData];
            }

            oLogin = nil;
            
        }
    }
    else if ([alertView.title isEqualToString:kFacebookLogoutTitle]) 
    {
        if (buttonIndex == 0) 
        {
            //취소
        }
        else
        {
            //확인
            [self.fb logout];
            
            
            //remove
            NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
            [defaults removeObjectForKey:kAccessToken];
            [defaults synchronize];
            
            DLog(@"accessToken : %@",self.accessToken);
            
            self.accessToken = nil;
        }
    }
    else if ([alertView.title isEqualToString:kTwittLogoutTitle]) 
    {
        if (buttonIndex == 0) 
        {
            //취소
        }
        else
        {
            //확인
            [self.oAuth forget];
            
            //remove
            [self.oAuth saveOAuthTwitterContextToUserDefaults:nil];
            
            self.oAuth = nil;
        }        
    }
    
    
    [self.tableView reloadData];
}


- (void)accessTokenFound:(NSString *)token {
    
    //DLog(@"Access token found: %@", token);
    
    self.accessToken = token;
    
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setValue:self.accessToken forKey:kAccessToken];
    
    
    
    
    DLog(@"self.accessToken : %@", self.accessToken);
    
    //_loginState = LoginStateLoggedIn;
    
    NSString *urlString = [NSString stringWithFormat:@"https://graph.facebook.com/me?access_token=%@", [self.accessToken stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSURL *url = [NSURL URLWithString:urlString];
    ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
    [request setDidFinishSelector:@selector(getFacebookProfileFinished:)];
    
    [request setDelegate:self];
    
    [request startAsynchronous];
    
    //테이블 뷰가 리로딩된다음에
    [self.tableView reloadData];
    
    //팝업 사라지게 하기 
    [self dismissModalViewControllerAnimated:YES]; 
    
    
    //[self getFacebookProfile];  
    //[self showLikeButton];
    //[self refresh];
}

- (void)getFacebookProfileFinished:(ASIHTTPRequest *)request
{
    // Use when fetching text data
    NSString *responseString = [request responseString];
    DLog(@"Got Facebook Profile: %@", responseString);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];   
    
    
    // NSString *email = [responseJSON objectForKey:@"email"];
    
    
    //NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    //[defaults setValue:email forKey:@"facebook_email"];
    
    
    NSString *username;
    NSString *firstName = [responseJSON objectForKey:@"first_name"];
    NSString *lastName = [responseJSON objectForKey:@"last_name"];
    if (firstName && lastName) {
        username = [NSString stringWithFormat:@"%@ %@", firstName, lastName];
    } else {
        username = @"mysterious user";
    }
    
    //설정 정보 셋팅 하기 
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setValue:username forKey:@"username"];
    
    
}

- (void)displayRequired {
    
    if (self.fb != nil) {
        [self presentModalViewController:self.fb animated:YES];
    }
    
}

- (void)closeTapped {
    [self.fb dismissModalViewControllerAnimated:YES];
    //_loginState = LoginStateLoggedOut;        
    [self.fb logout];
    
    //[fb release]; 
    
    self.fb = nil;
    //[self refresh];
}

#pragma mark -
#pragma mark TwitterLoginPopupDelegate

- (void)twitterLoginPopupDidCancel:(TwitterLoginPopup *)popup {
    [self.twitter.activityIndicator stopAnimating];
    
    [popup dismissModalViewControllerAnimated:YES];        
    [self.twitter release]; 
    self.twitter = nil; // was retained as ivar in "login"
}

- (void)twitterLoginPopupDidAuthorize:(TwitterLoginPopup *)popup {
    [oAuth saveOAuthTwitterContextToUserDefaults];
    
    //테이블 뷰가 리로딩된다음에
    [self.tableView reloadData];
    
    [popup dismissModalViewControllerAnimated:YES];        
    [self.twitter release]; 
    self.twitter = nil; // was retained as ivar in "login"
    //[self resetUi];
}

#pragma mark -
#pragma mark TwitterLoginUiFeedback

- (void) tokenRequestDidStart:(TwitterLoginPopup *)twitterLogin {
    DLog(@"token request did start");
    [self.twitter.activityIndicator startAnimating];
}

- (void) tokenRequestDidSucceed:(TwitterLoginPopup *)twitterLogin {
    DLog(@"token request did succeed");    
    [self.twitter.activityIndicator stopAnimating];
}

- (void) tokenRequestDidFail:(TwitterLoginPopup *)twitterLogin {
    DLog(@"token request did fail");
    [self.twitter.activityIndicator stopAnimating];
}

- (void) authorizationRequestDidStart:(TwitterLoginPopup *)twitterLogin {
    DLog(@"authorization request did start");    
    [self.twitter.activityIndicator startAnimating];
}

- (void) authorizationRequestDidSucceed:(TwitterLoginPopup *)twitterLogin {
    DLog(@"authorization request did succeed");
    [self.twitter.activityIndicator stopAnimating];
}

- (void) authorizationRequestDidFail:(TwitterLoginPopup *)twitterLogin {
    DLog(@"token request did fail");
    [self.twitter.activityIndicator stopAnimating];
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





@end
