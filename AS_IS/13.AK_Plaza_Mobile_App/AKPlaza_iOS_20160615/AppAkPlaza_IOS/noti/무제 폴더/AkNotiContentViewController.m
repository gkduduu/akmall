//
//  BarcodeModalViewController.m
//  UWCutomView
//
//  Created by 미영 신 on 11. 6. 6..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "AkNotiContentViewController.h"
#import "AlertMassageUtil.h"
#import "GlobalValues.h" 
#import "AkLoginWithNotiSettingView.h"
#import "JSON.h"
#import "ASIFormDataRequest.h"
#define kAccessToken @"accessToken"
@implementation AkNotiContentViewController
          
@synthesize bgImage,tabView, arrNotiContent, oModel, activityView;

- (void)dealloc
{
    [bgImage release];
    [tabView release];
    [arrNotiContent release];
    [oModel release];
    
    [activityView release];
    [super dealloc];
}

- (void)hideTabBar {
    
    //self.tabView.isTabBarHidden = NO;
	//self.tabView.tabBarHolder.hidden = YES;
}

- (void)showTabBar {
    //self.tabView.isTabBarHidden = YES;
	//self.tabView.tabBarHolder.hidden = NO;
}



-(void)doRightButton:(id)sender
{
    //[[self parentViewController] dismissModalViewControllerAnimated:YES];
    [self onTwitt:sender];
    
}

-(void)doLeftButton:(id)sender
{
    self.tabView.tabBarHolder.hidden = NO;
    [self.navigationController popViewControllerAnimated:YES];
}

//- (UINavigationController *)webViewAction
//{
//    return self.navigationController;
//}


//
//-(void)doLeftButton:(id)sender
//{
//    if (webView.canGoBack) {
//        [webView goBack];
//    }
//    else
//    {
//        
//        [self.navigationController popViewControllerAnimated:YES];
//        
//        [self.activityView stopAnimating];
//        [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
//    }
//    
//    
//}
//
//

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle
-(void)doCustomBtn:(id)sender
{
    self.tabView.tabBarHolder.hidden = YES;
}

// Implement loadView to create a view hierarchy programmatically, without using a nib.
- (void)loadView
{
   self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
    self.tabView=[[GlobalValues sharedSingleton] tabBar];
        //self.view.frame = CGRectMake(0, 0, 320, 480);
    
    
    //CGRect scrollViewFrame = [[UIScreen mainScreen]bounds]; 

    
    //CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.contentView.frame.size.width, self.contentView.frame.size.height);
    
    //UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:scrollViewFrame];
    
    
    //확대,축소 대응
    //scrollView.delegate = self;
    //scrollView.minimumZoomScale  = 0.5;
    //scrollView.maximumZoomScale  = 2.0;
    
    
    //self.bgImage = [[UIImageView alloc ] initWithImage:[UIImage imageNamed:@"missingtaiji.jpg"]];
    //CGSize imgSize = self.bgImage.image.size;
    //self.bgImage.contentMode = UIViewContentModeScaleAspectFit;
    //self.bgImage.userInteractionEnabled = YES;
    //스크롤뷰 frame의 width, height이 같아야함
    //self.bgImage.frame = CGRectMake(0, 0, imgSize.width, imgSize.height);
    //[scrollView addSubview:self.bgImage];
    //scrollView.scrollEnabled = YES;
    
    
    
    //실제 이미지 사이즈를 맞춰줘야함
    //scrollView.contentSize = imgSize;
    //bgMobileCard.frame.size;
    //[self.contentView addSubview:scrollView];
    
    
    //[scrollView release];
    
    //하단에 하양색~ 탭바 만큼~~ 어차피 바코드 모듈 붙일거니깐 우선 패스~
    //[self.tabBarController setWantsFullScreenLayout:YES];

}

//-(UIView *)viewForZoomingInScrollView:(UIScrollView *)scrollView
//{
//    for (id subview in scrollView.subviews) {
//        if([subview isKindOfClass:[UIImageView class]])
//        {
//            return subview;
//        }
//    }
//    
//    return nil;
//}


- (void)activityStartView:(UIActivityIndicatorView *)act
{
    //로딩 화면 출력될 때 ActivityIndicatorView 보이기
	self.activityView = act;
    
	self.activityView.center = CGPointMake(160, 169);
	
    [self.contentView insertSubview:self.activityView atIndex:999];
    
    [self.activityView startAnimating];
}


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"btn_top_pre" ofType:@"png"]] forState:UIControlStateNormal];
    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"btn_top_pre_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    
    
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"btn_sns" ofType:@"png"]] forState:UIControlStateNormal];
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"btn_sns_s" ofType:@"png"]] forState:UIControlStateHighlighted];
    
    //content webview url
    shortUrl = [NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:4]]];

    
    NSString *strNaviTitle = [NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]]];
    
    [self viewLblNaviTitle:strNaviTitle];
    

    
    self.oModel = [[AkNotiModel alloc] init];
    
    self.oModel.delegate = self;
    
    webView = [self.oModel performSelector:@selector(notiContentWebview:) withObject:shortUrl];
    
    webView.frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.contentView.frame.size.width, self.contentView.frame.size.height);
    
    [self.contentView insertSubview:webView atIndex:0];
    
    
    
    NSLog(@"%@", shortUrl);
    
    
    
}


- (UINavigationController *)webViewAction
{
    return self.navigationController;
}

- (void)viewDidUnload
{
    
    
    
    [super viewDidUnload];
    
    self.arrNotiContent = nil;
    self.tabView = nil;
    self.bgImage = nil;
    self.oModel = nil;
    
    self.activityView = nil;
    
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;

    
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


/*

- (void)getFacebookProfile {
    
    NSString *accessToken = [[NSUserDefaults standardUserDefaults] stringForKey:kAccessToken];
    
    NSString *urlString = [NSString stringWithFormat:@"https://graph.facebook.com/me?access_token=%@", [accessToken stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSURL *url = [NSURL URLWithString:urlString];
    ASIHTTPRequest *request = [ASIHTTPRequest requestWithURL:url];
    [request setDidFinishSelector:@selector(getFacebookProfileFinished:)];
    
    [request setDelegate:self];
    [request startAsynchronous];
}

- (void)getFacebookProfileFinished:(ASIHTTPRequest *)request
{
    // Use when fetching text data
    NSLog(@"Got Facebook Profile: %@", [request responseString]);
    
    
    NSString *accessToken = [[NSUserDefaults standardUserDefaults] stringForKey:kAccessToken];    
    
    NSString *urlString = [NSString stringWithFormat:@"https://graph.facebook.com/me/feed?access_token=%@", [accessToken stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    NSURL *url = [NSURL URLWithString:urlString];
    
    ASIFormDataRequest *newRequest = [ASIFormDataRequest requestWithURL:url];
    
    
    [newRequest setPostValue:@"ak message입니다." forKey:@"message"];
    
    [newRequest setPostValue:@"상세 화면으로 가기" forKey:@"name"];
    
    //[newRequest setPostValue:@"This tutorial shows you how to post to Facebook using the new Open Graph API." forKey:@"caption"];
    
    [newRequest setPostValue:@"http://img.akmall.com/imgs/common/new_head/h1_logo_akm.gif" forKey:@"picture"];
    
    [newRequest setPostValue:@"http://www.akmall.com" forKey:@"link"];

    //[newRequest setPostValue:@"From Ray Wenderlich's blog - an blog about iPhone and iOS development." forKey:@"description"];
    
    [newRequest setDidFinishSelector:@selector(postToWallFinished:)];
    
    [newRequest setDelegate:self];
    [newRequest startAsynchronous];            

}
*/


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
            
            NSString *name = [[NSString stringWithString:[self.arrNotiContent objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]]] retain];
            
            NSMutableDictionary *postInfo = [NSMutableDictionary
                                             dictionaryWithObject:[NSString stringWithFormat:@"@akmallcom %@ %@", name, tshortUrl]
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
            
            NSLog(@"Status posted. HTTP result code: %d", request.responseStatusCode);
            
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
        
        NSLog(@"accessToken : %@", accessToken);
        
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
    NSLog(@"responseString : %@", responseString);
    NSLog(@"JSONValue : %@", [responseString JSONValue]);
    
    NSMutableDictionary *responseJSON = [responseString JSONValue];
    NSString *postId = [responseJSON objectForKey:@"id"];
    NSLog(@"Post id is: %@", postId);
    
    if (postId) {
        MSG_NORMAL(@"등록 완료", @"확인");
    }
    else
    {
        MSG_NORMAL(@"등록 실패", @"확인");
    }
    
    
}



@end
