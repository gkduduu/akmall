//
//  AkMembersCardView.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "AkMembersCardView.h"
#import "AkMembersCardDetailView.h"




@implementation AkMembersCardView
@synthesize activityView;



#pragma mark - View lifecycle end
-(void)dealloc
{
    [activityView release];

    
    [super dealloc];
}

- (void)viewDidUnload
{
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



#pragma mark - event
- (void)doRightButton:(id)sender
{
    //[self.menuPopup.menuWebview reload];
    
    //팝업메뉴 띄우기 
    //[self.menuPopup animate:sender];
}



- (void)clickSubMyMileageListView:(id)sender
{
    AkMembersCardDetailView *subView = [[AkMembersCardDetailView alloc] init];
    subView.nWebViewType = 0;
    
    [self.navigationController pushViewController:subView animated:YES];
    
    
}
- (void)clickSubCardInfoView:(id)sender
{
    AkMembersCardDetailView *subView = [[AkMembersCardDetailView alloc] init];
    subView.nWebViewType = 1;
    
    [self.navigationController pushViewController:subView animated:YES];
}


#pragma mark - View lifecycle start


- (void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
}

-(void)viewWillAppear:(BOOL)animated
{
    //self.menuPopup.alpha = 0.0;
    
    
    
    OLogin *oLogin = [OLogin loadOLoginContextFromUserDefaults];
    

    
    if (oLogin.isLogin) 
    {
        

        
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
        lblTitle.text = @"멤버스카드";
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
        
        
        UIButton *btnMileage = [UIButton buttonWithType:UIButtonTypeCustom];
        
        
        [btnMileage setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"member_akmail" ofType:@"png"]] forState:UIControlStateNormal];
        [btnMileage setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"member_akmail_s" ofType:@"png"]] forState:UIControlStateHighlighted];
        
        btnMileage.frame = CGRectMake(0, 35, 160, 40);
        [btnMileage addTarget:self action:@selector(clickSubMyMileageListView:) forControlEvents:UIControlEventTouchUpInside];
        
        [self.contentView addSubview:btnMileage];
        
        btnMileage = nil;
        
        UIButton *btnCardInfo = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        [btnCardInfo setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"member_cardinfo" ofType:@"png"]] forState:UIControlStateNormal];
        [btnCardInfo setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"member_cardinfo_s" ofType:@"png"]] forState:UIControlStateHighlighted];
        btnCardInfo.frame = CGRectMake(160, 35, 160, 40);
        [btnCardInfo addTarget:self action:@selector(clickSubCardInfoView:) forControlEvents:UIControlEventTouchUpInside];
        
        [self.contentView addSubview:btnCardInfo];    
        
        btnCardInfo = nil;
        
        
        UIImageView *membersCardImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"member_card" ofType:@"png"]]];
        
        membersCardImg.frame = CGRectMake(38, 82, 244, 157);
        
        //카드 번호
        UILabel *lblBarcode = [[UILabel alloc] initWithFrame:CGRectMake(34, 73, 184, 19)];
        lblBarcode.backgroundColor = [UIColor clearColor];
        lblBarcode.text = @"발급받은 카드가 없습니다";
        lblBarcode.textColor = kFontColorWhite;
        lblBarcode.textAlignment = UITextAlignmentLeft;
        lblBarcode.font = kFontSizeDefault;
        
        [membersCardImg addSubview:lblBarcode];
        
        
        UILabel *lblMonthYear = [[UILabel alloc] initWithFrame:CGRectMake(83, 115, 62, 16)];
        lblMonthYear.backgroundColor = [UIColor clearColor];
        lblMonthYear.text = @"99/9999";
        lblMonthYear.textColor = kFontColorWhite;
        lblMonthYear.textAlignment = UITextAlignmentLeft;
        lblMonthYear.font = kFontSizeSub;
        
        [membersCardImg addSubview:lblMonthYear];    
        
        [self.contentView addSubview:membersCardImg];
        
        [membersCardImg release];
        membersCardImg = nil;
        
        [lblMonthYear release];
        lblMonthYear = nil;
        
        [lblBarcode release];
        lblBarcode = nil;
        
        
        //로그인했을때..@"member_barcode_bg" ㅠ
        
        
        UIImageView *membersBarcodeImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:nil ofType:@"png"]]];
        
        membersBarcodeImg.frame = CGRectMake(45, 242, 230, 77);
        
        [self.contentView addSubview:membersBarcodeImg];
        
        
        //로그인 안했을때..
        
        UILabel *lblInfo1 = [[UILabel alloc] initWithFrame:CGRectMake(0, 10, 230, 18)];
        lblInfo1.backgroundColor = [UIColor clearColor];
        lblInfo1.text = @"카드발급방법";
        lblInfo1.textColor = kFontBrownColor;
        lblInfo1.textAlignment = UITextAlignmentLeft;
        lblInfo1.font = kFontSizeDefault;
        
        [membersBarcodeImg addSubview:lblInfo1];
        
        UIImageView *textBuletImg1 = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
        
        textBuletImg1.frame = CGRectMake(0, 33, 11, 11);
        
        [membersBarcodeImg addSubview:textBuletImg1];
        
        UILabel *lblInfo2 = [[UILabel alloc] initWithFrame:CGRectMake(9, 28, 230, 18)];
        lblInfo2.backgroundColor = [UIColor clearColor];
        lblInfo2.text = @"인터넷발급 : 신청 2주일내 수령지 배송";
        lblInfo2.textColor = kFontBrownColor;
        lblInfo2.textAlignment = UITextAlignmentLeft;
        lblInfo2.font =  kFontSizeSub;
        
        [membersBarcodeImg addSubview:lblInfo2];   
        
        
        UIImageView *textBuletImg2 = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"text_bulet" ofType:@"png"]]];
        
        textBuletImg2.frame = CGRectMake(0, 51, 11, 11);
        
        [membersBarcodeImg addSubview:textBuletImg2];
        
        UILabel *lblInfo3 = [[UILabel alloc] initWithFrame:CGRectMake(9, 46, 230, 18)];
        lblInfo3.backgroundColor = [UIColor clearColor];
        lblInfo3.text = @"AK PLAZA 현장 발급 : 즉시 발급";
        lblInfo3.textColor = kFontBrownColor;
        lblInfo3.textAlignment = UITextAlignmentLeft;
        lblInfo3.font = kFontSizeSub;
        
        [membersBarcodeImg addSubview:lblInfo3];   
        
        
        
        UIImageView *membersCallImg = [[UIImageView alloc] initWithImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"member_call" ofType:@"png"]]];
        
        membersCallImg.frame = CGRectMake(45, 330, 230, 21);
        
        [self.contentView addSubview:membersCallImg];
    }
    else
    {
        LoginViewController *loginView = [[LoginViewController alloc] init];
        
        loginView.isLeftBtn = NO;
        
        [self.navigationController pushViewController:loginView animated:YES];
        
        loginView = nil;

    }
    
    oLogin = nil;
        
    

}


- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_all" ofType:@"png"]] forState:UIControlStateNormal];
    [self.btnRight setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_all_s" ofType:@"png"]] forState:UIControlStateHighlighted];

    
    

    
    
}



- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
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
