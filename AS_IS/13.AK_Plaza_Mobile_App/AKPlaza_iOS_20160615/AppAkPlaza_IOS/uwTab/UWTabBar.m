//
//  LPTabBar.m
//  YFPortal
//
//  Created by Jong Pil Park on 10. 11. 3..
//  Copyright 2010 Lilac Studio. All rights reserved.
//

#import "UWTabBar.h"
#import "UWTabBarItem.h"
#import "UWTabBarDefine.h"
#import "CustomBadge.h"
#import "AKAppDelegate.h"
#define kSelectedTab @"SelectedTAB"


@implementation UWTabBar

@synthesize initTab;
@synthesize delegate;
@synthesize tabBarHolder;
@synthesize tabViewControllers;
@synthesize tabItemsArray;
@synthesize isTabBarHidden;


- (void)dealloc {
	[tabBarHolder release];
	[tabViewControllers release];
	[tabItemsArray release];
	[super dealloc];
}


// 윈도우가 같은 크기의 뷰 생성하고 UIViewControllers와 TabBarItems의 배열 초기화.
- (id)initWithTabViewControllers:(NSMutableArray *)tbControllers tabItems:(NSMutableArray *)tbItems initialTab:(int)iTab {
	if ((self = [super init])) {
        

        
        //네비 사용시
		self.view.frame = [UIScreen mainScreen].bounds;
		initTab = iTab;
		
		NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
		if ([defaults integerForKey:kSelectedTab]) {
			initTab = [defaults integerForKey:kSelectedTab];
		}
		DLog(@"current tab index : %d", initTab);

        
        tabViewControllers = [[NSMutableArray alloc] initWithArray:tbControllers];
        tabItemsArray = [[NSMutableArray alloc] initWithArray:tbItems];
        
        [tbControllers release];
        tbControllers = nil;
        [tbItems release];
        tbItems = nil;
        
//        [[UIApplication sharedApplication] setStatusBarHidden:YES];
        

	}
    
    
    return self;
}


- (void)initialTab:(int)tabIndex {
	[self activateController:tabIndex];
	[self activateTabItem:tabIndex];
}



- (void)viewDidAppear:(BOOL)animated {
	[super viewDidAppear:animated];

    
	// 탭바 아이템을 담을 뷰 홀더 생성.
    //willChangeStatusBarFrame 메소드에 같은 frame셋팅하는 부분이 있다.
    //차후 tab bg 이미지를 받으면 해당 이미지 사이즈에 맞춰서 46인부분을 수정하면 된다.
    int nTabBgImgH = kTabBgImgHegiht;
    //DLog(@"(self.view.frame.size.height)-nTabBgImgH  : %f", (self.view.frame.size.height)-nTabBgImgH );
    //DLog(@"[[UIScreen mainScreen] bounds].origin.y  : %f", [[UIScreen mainScreen] bounds].origin.y );
	tabBarHolder = [[UIView alloc] initWithFrame:CGRectMake(self.view.frame.origin.x, (self.view.frame.size.height)-nTabBgImgH, self.view.frame.size.width, nTabBgImgH)];
	tabBarHolder.backgroundColor = [UIColor clearColor];
	
	// 탭바 백그라운드 이미지.
	UIImage *tabBg = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:ktabBarBgImage ofType:nil]];
    
	UIImageView *tabImageView = [[UIImageView alloc] initWithImage:tabBg];
	[tabBarHolder addSubview:tabImageView];
	[tabImageView release];
	
	//add it as a subview
	[self.view addSubview:tabBarHolder];
	// 루프를 돌며 모든 뷰 컨트롤러들에게 뷰를 추가.
	for (int i = [tabViewControllers count]-1; i >= 0; i--) {
        if (!([tabViewControllers objectAtIndex:i] == [NSNull null])) 
        {
            [self.view addSubview:[[tabViewControllers objectAtIndex:i] view]];
        }
	}
    
	// 루프를 돌며 모든 탭바 아이템을 tabBarHolder에 추가.
	for (int i = [tabItemsArray count] - 1; i >= 0; i--) {
		[[tabItemsArray objectAtIndex:i] setDelegate:self];
		[self.tabBarHolder addSubview:[tabItemsArray objectAtIndex:i]];
		// 초기화활 탭 확인.
		if (i == initTab) {
			[[tabItemsArray objectAtIndex:i] toggleOn:YES];
		}
	}
	[self.view bringSubviewToFront:tabBarHolder];
	// 탭바를 감추거나 보여주고 초기화할 탭을 결정.
//	[self initialTab:initTab];
	[self initialTab:0];
    
    //배지의 카운트 설정
    [self setBadge:[GlobalValues getNotice]];
}


// 루프를 돌며 모튼 탭바 아이템의  상태 설정: YES/NO.
- (void)activateTabItem:(int)index {
	for (int i = [tabItemsArray count]; i < [tabItemsArray count]; i++) {
		if (i == index) {
			[[tabItemsArray objectAtIndex:i] toggleOn:YES];
		} 
		else {
			[[tabItemsArray objectAtIndex:i] toggleOn:NO];
		}
	}
}


// 루프를 돌며 모든 UIViewControllers 아이템의 상태 설정: YES/NO.
- (void)activateController:(int)index {
	for (int i = 0; i < [tabViewControllers count]; i++) {
        
        if (!([tabViewControllers objectAtIndex:i] == [NSNull null])) {
            if (i == index) {
                [[tabViewControllers objectAtIndex:i] view].hidden = NO;
                
                

            } 
            else {
                
                [[tabViewControllers objectAtIndex:i] view].hidden = YES;
                
                
                
                
            }
            
            
            
            if (i==index) {
                [[tabViewControllers objectAtIndex:i] popToRootViewControllerAnimated:YES];
                
                //VIEWWILLAPPEAR is CALLED ...
                //[[tabViewControllers objectAtIndex:i] viewDidLoad];
                [[tabViewControllers objectAtIndex:i] viewWillAppear:YES];
                
                
                //akmall 일때 구분할려구, qr코드찍었을때 url이 akmall인지, akmall전환버튼 눌렀을때의 url이 akmall인지 
                [[NSUserDefaults standardUserDefaults] setBool:NO   forKey: @"isQRCode"];
                
                //viewWillAppear
            }
            /*
            else
            {
                [[[tabViewControllers objectAtIndex:i] view] removeFromSuperview];
            }
            */
 
        }



	}
}

// 프로토콜은 버튼과 탭바 사의의 통신을 담당한다.
#pragma mark -
#pragma mark UWTabbarItem action

- (void)selectedItem:(UWTabBarItem *)button {
	int indexC = 0;
	NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSUInteger tabIndex = -1;
    NSUInteger nPrevSelected = -1;
    
	for (UWTabBarItem *tb in tabItemsArray) {
        [tb toggleOn:NO];
        break;
        //인터넷백화점이면.. (사파리로 띄우기)

        if (tb == button) {
            
            if (tb.tag == 2) 
            {
                //전 탭의 index
                nPrevSelected = [defaults integerForKey:kSelectedTab];
                
                tabIndex = nPrevSelected;
                
                //인터넷백화점을 중심으로 왼쪽메뉴일때..
                [[tabItemsArray objectAtIndex:tabIndex] toggleOn:YES];
                
                [tb toggleOn:NO];
            }
            else
            {
                
                [tb toggleOn:YES];
                
                tabIndex = indexC;
                
            }
            
            //DLog(@"TabItem index : %d", tabIndex);
            
//            [self activateController:tabIndex];
            [defaults setInteger:tabIndex forKey:kSelectedTab];
            
        } 
        else {
            
            if (tabIndex == indexC) 
            {
                //인터넷백화점을 중심으로 오른쪽메뉴일때..
                [tb toggleOn:YES];
            }
            else
            {
                [tb toggleOn:NO];
            }
            
        }
        
		indexC++;
        
        
	}	 
}



-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    if (event.type == UIEventTypeTouches ) {
        if (isTabBarHidden) {
            [self.delegate hideTabBar];
        }
        else
        {
            [self.delegate showTabBar];
        }
    }
    
}


- (void)setBadge:(int)cnt
{
    if(customBadge == nil){
    customBadge = [CustomBadge customBadgeWithString:[NSString stringWithFormat:@"%d",cnt]
                                     withStringColor:[UIColor whiteColor]
                                      withInsetColor:[UIColor redColor]
                                      withBadgeFrame:YES
                                 withBadgeFrameColor:[UIColor clearColor]
                                           withScale:0.8
                                         withShining:YES];
    customBadge.frame = CGRectMake(253-customBadge.frame.size.width, 0, customBadge.frame.size.width, customBadge.frame.size.height);
    [tabBarHolder addSubview:customBadge];
    } else {
        if ([customBadge respondsToSelector:@selector(autoBadgeSizeWithString:)]) {
            [customBadge autoBadgeSizeWithString:[NSString stringWithFormat:@"%d",cnt]];
        }
        
    }
    
    if(cnt == 0){
        if ([customBadge respondsToSelector:@selector(setHidden:)]) {
            customBadge.hidden = YES;
        }
        
    } else {
        if ([customBadge respondsToSelector:@selector(setHidden:)]) {
            customBadge.hidden = NO;
        }

    }
}

@end
