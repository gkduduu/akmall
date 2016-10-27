//
//  UWNavigation.m
//  AppAkPlaza_IOS
//
//  Created by  on 11. 12. 21..
//  Copyright (c) 2011년 __MyCompanyName__. All rights reserved.
//

#import "UWNavigation_web.h"


@implementation UWNavigation_web


@synthesize naviBackgroud;
@synthesize btnLeft;
@synthesize btnRight;
@synthesize naviTitle;
@synthesize contentView;


- (void)dealloc
{
    
    //modifying layer that is being finalized
    //위 에러가 나는 경우는.. 네이버에 찾아보면 다 나옴~ 참고하세요~
    [self.btnLeft removeFromSuperview];
    [self.btnRight removeFromSuperview];
    [self.naviTitle removeFromSuperview];
    [self.naviBackgroud removeFromSuperview];
    [self.contentView removeFromSuperview];
    

    
    self.btnLeft = nil;
    self.btnRight = nil;
    self.naviTitle = nil;
    self.naviBackgroud = nil;
    self.contentView = nil;

    
    
    [super dealloc];
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
    self.view.frame = [[UIScreen mainScreen] bounds];
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    

    
}




- (void)viewDidUnload
{
    [self setBtnLeft:nil];
    [self setBtnRight:nil];
    [self setNaviTitle:nil];
    [self setNaviBackgroud:nil];
    [self setContentView:nil];
    

    
    
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (IBAction)doLeftButton:(id)sender {
}

- (IBAction)doRightButton:(id)sender {
}

-(void)viewLblNaviTitle:(NSString *)title
{
    [self.naviTitle setImage:nil];
    
    for (UILabel *view in [self.naviTitle subviews]) {
        [view removeFromSuperview];
    }
    
    
    UILabel *lblNaviTitle = [[[UILabel alloc] initWithFrame:CGRectMake(0, 0, self.naviTitle.frame.size.width, self.naviTitle.frame.size.height)] autorelease];
    [lblNaviTitle setFont:[UIFont boldSystemFontOfSize:21.0]];
    lblNaviTitle.text = title;
    lblNaviTitle.textAlignment = UITextAlignmentCenter;
    lblNaviTitle.alpha = 1.0;
    lblNaviTitle.backgroundColor = [UIColor clearColor];
    lblNaviTitle.textColor = [UIColor whiteColor];
    
    
    [self.naviTitle addSubview:lblNaviTitle];
    
}
@end
