//
//  BarcodeModalViewController.m
//  UWCutomView
//
//  Created by 미영 신 on 11. 6. 6..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "AkNotiContentViewController.h"
#import "AlertMassageUtil.h"

@implementation AkNotiContentViewController

@synthesize oModel;
@synthesize strTitle;
@synthesize activityView;
@synthesize webView;
@synthesize strShortURL;
@synthesize arrNotiContent;

- (void)dealloc
{
    [oModel release];
    [strTitle release];
    
    [activityView release];
    [webView release];
    
    [strShortURL release];
    
    [arrNotiContent release];
    
    [super dealloc];
}



- (void)viewDidUnload
{
    
    self.oModel = nil;
    self.strTitle = nil;
    
    self.activityView = nil;
    self.webView = nil;
    
    self.strShortURL = nil;
    
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


-(void)doRightButton:(id)sender
{
    //[[self parentViewController] dismissModalViewControllerAnimated:YES];
    
    [self performSelector:@selector(onTwitt:) withObject:sender ];
    
}

-(void)doLeftButton:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle


// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView
{
   self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
    
    
    //self.view.frame = CGRectMake(0, 0, 320, 480);
    
    CGRect frame = CGRectMake(0.0, 35.0, 320, 328);
    
    self.oModel = [[AkNotiModel alloc] init];
    
    self.oModel.delegate = self;
    
    self.strShortURL = [NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:3]]];
    
    self.webView = [self.oModel performSelector:@selector(notiContentWebview:) withObject:self.strShortURL];
    
    self.webView.frame = frame;
    
    [self.contentView insertSubview:self.webView atIndex:0];

}







// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_sns" ofType:@"png"]] forState:UIControlStateNormal];
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_sns_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    
    
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
    
    NSString *strNaviTitle = [NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]]];
    
    
    lblTitle.text = strNaviTitle;
    
    
    lblTitle.textColor = kFontTitleColor;
    lblTitle.textAlignment = UITextAlignmentLeft;
    lblTitle.font = kFontSizeTitle;
    
    [titleView addSubview:lblTitle];
    
    [self.contentView addSubview:titleView];
    
    [lblTitle release];
    lblTitle = nil;
    [titleView release];
    titleView = nil;
    [titleView release];
    titleView = nil;

    
    //각 지점별 알림 처리 후 ...
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"PID"];
    [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"BC"];

    
}



- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}


#pragma mark - UIActionSheet 함수
- (void)onTwitt:(id)sender {
    
    UIActionSheet *asTwitt = nil;
    
    asTwitt = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"취소" destructiveButtonTitle:@"Twitt" otherButtonTitles:@"Facebook"     , nil];
    
    asTwitt.destructiveButtonIndex = 0;
    asTwitt.actionSheetStyle = UIActionSheetStyleBlackTranslucent;
    
    [asTwitt showInView:[UIApplication sharedApplication].keyWindow];
    [asTwitt release];
}




-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    //sns가 둘다 로그인이 안되있으면... 알럿으로 버튼 세개로 [트위터 연동][페이스북연동][취소] 나타내기
    
    if (buttonIndex == 0) {
        
        
        //트위터 
        
        OAuth *oAuth = [[OAuth alloc] initWithConsumerKey:OAUTH_CONSUMER_KEY andConsumerSecret:OAUTH_CONSUMER_SECRET];
        
        
        [oAuth loadOAuthTwitterContextFromUserDefaults];
        
        if (oAuth.oauth_token_authorized) 
        { 
            tshortUrl = [NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:6]]];
            
            NSString *postUrl = @"https://api.twitter.com/1/statuses/update.json";
            
            ASIFormDataRequest *request = [[ASIFormDataRequest alloc]
                                           initWithURL:[NSURL URLWithString:postUrl]];
            //타이틀  
            NSString *name = [[NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]]] retain];
            
            DLog(@"트위터 메세지 :   @akplazacom %@ %@", name, tshortUrl);
            
            NSMutableDictionary *postInfo = [NSMutableDictionary
                                             dictionaryWithObject:[NSString stringWithFormat:@"@akplazacom %@ %@", name, tshortUrl]
                                             forKey:@"status"];
            
            
            for (NSString *key in [postInfo allKeys]) {
                [request setPostValue:[postInfo objectForKey:key] forKey:key];
            }
            
            [request addRequestHeader:@"Authorization"
                                value:[oAuth oAuthHeaderForMethod:@"POST"
                                                           andUrl:postUrl
                                                        andParams:postInfo]];
            
            [request startSynchronous];
            
            if (request.responseStatusCode == 200) 
            {
                MSG_NORMAL(@"등록 완료", @"확인");
            }
            else if (request.responseStatusCode == 403) 
            {
                MSG_NORMAL(@"이미 작성한 트윗입니다.", @"확인");
            }
            else
            {
                MSG_NORMAL(@"등록 실패", @"확인");
            }
            
            DLog(@"Status posted. HTTP result code: %d", request.responseStatusCode);
            
            [request release];
            
            
            
        }
        else
        {
            AkLoginWithNotiSettingView *oAkSnsSetViewController= [[AkLoginWithNotiSettingView alloc] init];
            
            
            [self.navigationController pushViewController:oAkSnsSetViewController animated:YES];       
        }
        
        [oAuth release];
        oAuth = nil;
        
    }
    else if(buttonIndex == 1)
    {
        //페이스북 
        NSString *accessToken = [[NSUserDefaults standardUserDefaults] stringForKey:kAccessToken];
        
        DLog(@"accessToken : %@", accessToken);
        
        if (accessToken == nil) {
            AkLoginWithNotiSettingView *oAkSnsSetViewController= [[AkLoginWithNotiSettingView alloc] init];
            
            
            [self.navigationController pushViewController:oAkSnsSetViewController animated:YES];      
        }
        else
        {
            fshortUrl = [NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:5]]];
            
            //[self getFacebookProfile];
            
            NSString *accessToken = [[NSUserDefaults standardUserDefaults] stringForKey:kAccessToken];    
            
            NSString *urlString = [NSString stringWithFormat:@"https://graph.facebook.com/me/feed?access_token=%@", [accessToken stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            NSURL *url = [NSURL URLWithString:urlString];
            
            ASIFormDataRequest *newRequest = [ASIFormDataRequest requestWithURL:url];
            
            
            //[newRequest setPostValue:@"ak message입니다." forKey:@"message"];
            
            NSString *name = [[NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]]] retain];
            
            
            [newRequest setPostValue:name forKey:@"name"];
            
            //[newRequest setPostValue:@"This tutorial shows you how to post to Facebook using the new Open Graph API." forKey:@"caption"];
            
            //[newRequest setPostValue:@"http://img.akmall.com/imgs/common/new_head/h1_logo_akm.gif" forKey:@"picture"];
            
            [newRequest setPostValue:fshortUrl forKey:@"link"];
            
            //[newRequest setPostValue:@"From Ray Wenderlich's blog - an blog about iPhone and iOS development." forKey:@"description"];
            
            [newRequest setDidFinishSelector:@selector(postToWallFinished:)];
            
            [newRequest setDelegate:self];
            [newRequest startAsynchronous]; 
            
            
            
            
        }
        
        
        
        
        
    }
    else if(buttonIndex == 2)
    {
        MSG_NORMAL(@"취소되었습니다", @"확인") ;
    }
    
    
    
}


- (void)postToWallFinished:(ASIHTTPRequest *)request
{
    // Use when fetching text data
    NSString *responseString = [request responseString];
    DLog(@"responseString : %@", responseString);
    DLog(@"JSONValue : %@", [responseString JSONValue]);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];
    NSString *postId = [responseJSON objectForKey:@"id"];
    DLog(@"Post id is: %@", postId);
    
    if (postId) {
        MSG_NORMAL(@"등록 완료", @"확인");
    }
    else
    {
        MSG_NORMAL(@"등록 실패", @"확인");
    }
    
    
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
