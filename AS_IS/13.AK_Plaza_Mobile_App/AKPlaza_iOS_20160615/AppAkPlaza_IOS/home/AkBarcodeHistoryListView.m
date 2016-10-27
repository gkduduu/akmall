//
//  AkNotiListBoxViewController.m
//  AppAkPlaza_iOS
//
//  Created by uniwis on 11. 6. 13..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "AkBarcodeHistoryListView.h"
#import "AkNotiContentViewController.h"
#import "AKAppDelegate.h"

#define kPageDivide  6  

@implementation AkBarcodeHistoryListView


@synthesize tableView,  arrNotilist, nPageNum, activityIndicator;


- (void)dealloc
{

    [tableView release];
    [arrNotilist release];
    [activityIndicator release];
    
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}
 
#pragma mark - event
-(void)doLeftButton:(id)sender
{
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@?act=index", kAddressURL ]];
    
    NSURLRequest *reqURL = [NSURLRequest requestWithURL:url];
    UWTabBar *tabBar = [[GlobalValues sharedSingleton] tabBar];
    tabBar.currentIndex = 0;
    [tabBar.webView loadRequest:reqURL];
    [self.navigationController popToRootViewControllerAnimated:YES];
}

/*
-(void)loadData
{
    
    if (nPageNum == 1) {
        if (self.arrItemList && [self.arrItemList count] > 0) {
            
            //처음 페이지이면, removeAllObjects한다음에 채우기. 
            [self.parsedItems removeAllObjects];
            
            self.parsedItems = [[self.arrItemList objectAtIndex:0] retain] ;
        }
    }
    else
    {
        if (self.arrItemList && [self.arrItemList count] > 0) {
            
            //처음 페이지가 아니면 기존 배열에 누적해서 채우기 
            
            for (NSMutableArray *arr in [self.arrItemList objectAtIndex:0]) {
                
                
                [self.parsedItems addObject:arr];
            }
            
        }
    }
    
    
    
    
    
    NSMutableArray *nextPageItems = nil;
    NSString *result = @"NO";
    
    if (self.arrItemList && [self.arrItemList count] > 0) {
        
        nextPageItems = [[self.arrItemList objectAtIndex:1] retain];
        
        
        NSMutableDictionary *items = [[NSMutableDictionary alloc] initWithDictionary:[nextPageItems objectAtIndex:0]];
        
        result = [items objectForKey:[[kfashionElements objectForKey:[kfashion_Items objectAtIndex:1]]  objectAtIndex:0]];
        
        [items release];
        
        items = nil;
        
        [nextPageItems release];
        nextPageItems = nil;
    }
    
    self.isNextPageItems = NO;
    
    if ([result isEqualToString:@"YES"]) 
    {
        self.isNextPageItems = YES;
    }
    else
    {
        self.isNextPageItems = NO;
    }
}
*/
-(void)clickMore:(id)sender
{
    //more
    nPageNum++;
    
    
    int nTotalCnt = [self.arrNotilist count];

    if (nTotalCnt > kPageDivide) 
    {
        
    
        [self.tableView reloadData];
        
        
        //더보기 할때 테이블뷰 스크롤의 포지션이 아래에서 위로 올라가게끔 .. 
        
        
        int currRow;
        
        
        if ((nPageNum * kPageDivide) < nTotalCnt)
        {
            
            currRow = (nPageNum * kPageDivide);
            
        }
        else
        {
            currRow = [self.arrNotilist count];
        }
        
        
        
        NSIndexPath *idxPath = [NSIndexPath indexPathForRow:currRow-1 inSection:0];
        
        
        
        
        [self.tableView scrollToRowAtIndexPath:idxPath atScrollPosition:UITableViewScrollPositionBottom animated:YES];
    }
}

#pragma mark - View lifecycle
-(void)loadView
{
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
    
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back" ofType:@"png"]] forState:UIControlStateNormal];
//    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"navi_btn_back_s" ofType:@"png"]] forState:UIControlStateHighlighted];
}



- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSError *error;
    
    //DB 객체 가져오기 
    AKAppDelegate *appDelegate = (AKAppDelegate *)[[UIApplication sharedApplication] delegate];
	NSManagedObjectContext *managedObjectContext = appDelegate.managedObjectContext;
    
    
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSFetchRequest *delRequest = [[NSFetchRequest alloc] init];
	NSEntityDescription *entityDescription = [NSEntityDescription entityForName:@"QRCodeHistoryTable" inManagedObjectContext:managedObjectContext];
	[request setEntity:entityDescription];
	[delRequest setEntity:entityDescription];

    
    
    //날짜 구하기 : 데이터 로딩할때 90일 이후 데이터는 삭제하기 ///////////////////////////////////////////////////////////////////////////
    NSDate *dateAfter90;
    NSDate *crrDate = [NSDate dateWithTimeIntervalSinceNow:60];
    
    
    NSCalendar *comp_gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
    unsigned int countUnitFlags = NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit | NSHourCalendarUnit | NSMinuteCalendarUnit | NSSecondCalendarUnit;
    NSDateComponents *dateAfter90Components = [comp_gregorian components:countUnitFlags fromDate:crrDate];
    [comp_gregorian release];
    comp_gregorian = nil;
    
    
    
    [dateAfter90Components setYear:dateAfter90Components.year];
    [dateAfter90Components setMonth:dateAfter90Components.month];
    [dateAfter90Components setDay:dateAfter90Components.day - 90];
    
    dateAfter90 = [[NSCalendar currentCalendar] dateFromComponents:dateAfter90Components];
    
    DLog(@"dateAfter90 String: %@", dateAfter90);//dateAfter90 String: 2011-10-04 08:34:42 +000
    
    //에러남 [dateAfter90Components release];
    dateAfter90Components = nil;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    //검색조건 : 데이터 로딩할때 90일 이전 데이터는 삭제  //predicate release하면 에러난다. autorelease해주나 부당 
    NSPredicate *delPredicate = [NSPredicate predicateWithFormat:@"createDate < %@", dateAfter90];
    [delRequest setPredicate:delPredicate];
    
    NSMutableArray *delFetchResults = [[managedObjectContext executeFetchRequest:delRequest error:&error] mutableCopy];
    
    if (delFetchResults != nil) {
        //삭제 
        for (NSManagedObject* delItem in delFetchResults) {
            [managedObjectContext deleteObject:delItem];
            delItem = nil;
        }
    }
    else
    {
        //에러 
        
    }
    
    
    [managedObjectContext save:&error];
    
    [delFetchResults release];
    delFetchResults = nil;
    
    delPredicate = nil;
    
    [delRequest release];
    delRequest = nil;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    //페이징 처리하기 
    nPageNum = 1;
    
    
    
    /*
    AKAppDelegate *appDelegate = (AKAppDelegate *)[[UIApplication sharedApplication] delegate];
	NSManagedObjectContext *managedObjectContext = appDelegate.managedObjectContext;
    
    
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
	NSEntityDescription *entityDescription = [NSEntityDescription entityForName:@"QRCodeHistoryTable" inManagedObjectContext:managedObjectContext];
	[request setEntity:entityDescription];
	*/
    
    //검색조건 : 데이터 로딩할때 90일 이후 데이터는 안나오게 
    //NSPredicate *predicate = [NSPredicate predicateWithFormat:@"LIMIT %d OFFSET %d", 1, 6];
    //[request setPredicate:predicate];
    
    //sort =  ascending:YES (오름차순)  ascending:NO (내림차순)
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"createDate" ascending:NO];
	NSArray *sortDescriptors = [[NSArray alloc] initWithObjects:sortDescriptor, nil];
	[request setSortDescriptors:sortDescriptors];
	[sortDescriptors release];
    sortDescriptors = nil;
	[sortDescriptor release];
	sortDescriptor = nil;
    
    
    
    //
	NSMutableArray *mutableFetchResults = [[managedObjectContext executeFetchRequest:request error:&error] mutableCopy];
	if (mutableFetchResults != nil) {
		
        //검색된 객체관리 하기 
        self.arrNotilist = mutableFetchResults;
	}
    else
    {
        // Handle error
        self.arrNotilist = nil;
    }
	
	
	[mutableFetchResults release];
    mutableFetchResults = nil;
	[request release];
    request = nil;
    
    //data////////////////////////////////////////////////////////////////////////////////////////////
    /*
    NSString *path = [[NSBundle mainBundle] bundlePath];
    NSString *finalPath = [path stringByAppendingPathComponent:@"testBarcodeHistoryData.plist"];
    NSDictionary *outlineData = [[NSDictionary dictionaryWithContentsOfFile:finalPath] retain];
    
    self.arrNotilist = [outlineData objectForKey:@"BarcodeHistoryList"];
    
    [outlineData release];
     */
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
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
    lblTitle.text = @"히스토리";
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
    
    CGFloat height = [[UIScreen mainScreen]applicationFrame].size.height;
    CGRect tableViewFrame = CGRectMake(0.0, 35.0, 320, height-172-40);
//  2013.11.14 changuk 수정
//    CGRect tableViewFrame = CGRectMake(0.0, 35.0, 320, 288);
    self.tableView = [[UITableView alloc ] initWithFrame:tableViewFrame style:UITableViewStylePlain];
    self.tableView.delegate = self;
    self.tableView.dataSource = self; 
    self.tableView.opaque = NO;
    self.tableView.backgroundColor = kBgColor;
    [self.tableView setSeparatorColor:kTableViewSeparatorColor];
    if ([self.tableView respondsToSelector:@selector(setSeparatorInset:)]) {
        [self.tableView setSeparatorInset:UIEdgeInsetsZero];
    }
    [self.contentView addSubview:self.tableView];
    
    [self.tableView reloadData];
    
    
    UIButton *moreImg = [UIButton buttonWithType:UIButtonTypeCustom];
    
    [moreImg setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"list_more" ofType:@"png"]] forState:UIControlStateNormal];
    
    [moreImg setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"list_more" ofType:@"png"]] forState:UIControlStateHighlighted];
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7) {
        moreImg.frame = CGRectMake(0, height-20-147-40, 320, 48);
    } else {
        moreImg.frame = CGRectMake(0, height-147-40, 320, 48);
    }
    
    //    2013.11.14 changuk 수정
    //    moreImg.frame = CGRectMake(0, 313, 320, 48);
    
    [moreImg addTarget:self action:@selector(clickMore:) forControlEvents:UIControlEventTouchUpInside];
    
    [self.contentView addSubview:moreImg];
    
 
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    self.tableView = nil;
    self.arrNotilist = nil;
    self.activityIndicator = nil;
    
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    //#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 1;
}


-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
//    UIImage *image = nil;
//    UIImage *image_s = nil;
//    
//    image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"list_bg" ofType:@"png"]];
//    image_s = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"list_bg_s" ofType:@"png"]];
//    
//    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
//    UIImageView* imgView_s = [[UIImageView alloc] initWithImage:image_s];
//    
//    cell.backgroundView = imgView;
//    cell.selectedBackgroundView = imgView_s;
//    
//    [imgView release];
//    [imgView_s release];
    
    //selected 됬을때... 
    
    /*
    cell.textLabel.textColor  = kFontDarkBrownColor;
    cell.detailTextLabel.textColor  = kFontGrayColor;
    
    cell.detailTextLabel.highlightedTextColor  = kFontGrayColor;
    cell.textLabel.highlightedTextColor  = kFontDarkBrownColor;
    
    
    cell.textLabel.font = kFontSizeLarge;
    cell.detailTextLabel.font = kFontSizeSub;
     */
    
    //selected 됬을때... 
    cell.textLabel.backgroundColor = [UIColor clearColor];
    cell.detailTextLabel.backgroundColor = [UIColor clearColor];

}



#pragma mark -
#pragma mark Configuring table view cells

#define ROW_HEIGHT 56

#define NAME_TAG 1
#define TIME_TAG 2
#define IMAGE_TAG 3

#define LEFT_COLUMN_OFFSET 10.0
#define LEFT_COLUMN_WIDTH 210.0

#define MIDDLE_COLUMN_OFFSET 225.0
#define MIDDLE_COLUMN_WIDTH 70.0

#define RIGHT_COLUMN_OFFSET 300.0

#define MAIN_FONT_SIZE 18.0

#define LABEL_HEIGHT 50.0

#define IMAGE_SIDE_w 11.0
#define IMAGE_SIDE_h 18.0

- (UITableViewCell *)tableViewCellWithReuseIdentifier:(NSString *)identifier {
	
	/*
	 Create an instance of UITableViewCell and add tagged subviews for the name, local time, and quarter image of the time zone.
	 */
    
	UITableViewCell *cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier] autorelease];
    
	/*
	 Create labels for the text fields; set the highlight color so that when the cell is selected it changes appropriately.
     */
	UILabel *label;
	CGRect rect;
	
	// Create a label for the time zone name.
	rect = CGRectMake(LEFT_COLUMN_OFFSET, (ROW_HEIGHT - LABEL_HEIGHT) / 2.0, LEFT_COLUMN_WIDTH, LABEL_HEIGHT);
	label = [[UILabel alloc] initWithFrame:rect];
	label.tag = NAME_TAG;
	label.font = kFontSizeLarge;
    label.numberOfLines = 2;
    label.backgroundColor = [UIColor clearColor];
	label.adjustsFontSizeToFitWidth = YES;
	[cell.contentView addSubview:label];
    label.textColor = kFontDarkBrownColor;
	label.highlightedTextColor = kFontDarkBrownColor;
	[label release];
	
	// Create a label for the time.
	rect = CGRectMake(MIDDLE_COLUMN_OFFSET, (ROW_HEIGHT - LABEL_HEIGHT) / 2.0, MIDDLE_COLUMN_WIDTH, LABEL_HEIGHT);
	label = [[UILabel alloc] initWithFrame:rect];
	label.tag = TIME_TAG;
	label.font = kFontSizeSub;
	label.textAlignment = UITextAlignmentRight;
    label.backgroundColor = [UIColor clearColor];
	[cell.contentView addSubview:label];
    label.textColor = kFontAccSaColor;
	label.highlightedTextColor = kFontGrayColor;
	[label release];
    
	// Create an image view for the quarter image.
	rect = CGRectMake(RIGHT_COLUMN_OFFSET, (ROW_HEIGHT - IMAGE_SIDE_h) / 2.0, IMAGE_SIDE_w, IMAGE_SIDE_h);
    
	UIImageView *imageView = [[UIImageView alloc] initWithFrame:rect];
	imageView.tag = IMAGE_TAG;
	[cell.contentView addSubview:imageView];
	[imageView release];	
	
	return cell;
}


- (void)configureCell:(UITableViewCell *)cell forIndexPath:(NSIndexPath *)indexPath {
    
    
    
    UILabel *label;
	
	// Set the locale name.
	label = (UILabel *)[cell viewWithTag:NAME_TAG];
    
    
    NSManagedObject* item = [self.arrNotilist objectAtIndex:indexPath.row];
    NSDate *createDate = [item valueForKey:@"createDate"];
    //cell.detailTextLabel.text = [self _getCurrentStringTime:createDate];
    //cell.textLabel.text = [item valueForKey:@"url"];
    //cell.textLabel.numberOfLines = 2;

    
    
    
    label.text = [item valueForKey:@"url"];
    
    
    label = (UILabel *)[cell viewWithTag:TIME_TAG];
    label.text = [self _getCurrentStringTime:createDate];
    
    
    //액세서리 뷰 이미지 바꾸기~ 
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow" ofType:@"png"]];
    
    UIImageView *imageView = (UIImageView *)[cell viewWithTag:IMAGE_TAG];
    
    imageView.image = image;
    
    //}
    
    item = nil;
    image = nil;
    
    
    
    
    
}





-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 56;
}






- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    //#warning Incomplete method implementation.
    // Return the number of rows in the section.
    
    
    int nTotalCnt = [self.arrNotilist count];
    
    if ((nPageNum * kPageDivide) < nTotalCnt)
    {
        
        return (nPageNum * kPageDivide);
        
    }
    else
    {
        return [self.arrNotilist count];
    }
     
    
    //return [self.arrNotilist count];
    
}

//#define REFRESH_TIME_FORMAT                         @"yyyy년 MM월 dd일 (HH:mm:ss)"
#define REFRESH_TIME_FORMAT                         @"yyyy.MM.dd"
// 최근 새로고침 시간을 String형으로 반환
- (NSString *)_getCurrentStringTime:(NSDate*)createDate
{
    //NSTimeInterval timeStamp = [[NSDate date] timeIntervalSince1970];
    NSDate *date = createDate;
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:REFRESH_TIME_FORMAT];
    
    return [dateFormatter stringFromDate:date];
}



- (UITableViewCell *)tableView:(UITableView *)tView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (cell == nil) {
		cell = [self tableViewCellWithReuseIdentifier:CellIdentifier];
	}
	
	// configureCell:cell forIndexPath: sets the text and image for the cell -- the method is factored out as it's also called during minuted-based updates.
	[self configureCell:cell forIndexPath:indexPath];
	return cell;
    
    
    
    /*
    
    UITableViewCell *cell = [tView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:CellIdentifier] autorelease];
    }
     
    
    cell.textLabel.backgroundColor = [UIColor clearColor];
    cell.detailTextLabel.backgroundColor = [UIColor clearColor];
    
    */
    
    /*
    BOOL isCell = NO;
    
    if ((nPageNum * kPageDivide) < [arrNotilist count]) {
        
        if(indexPath.row != (nPageNum * kPageDivide))
        {

            isCell = YES;

        }
        else
        {

            
            cell.textLabel.text = @"Load More";
            cell.detailTextLabel.text = nil;
            cell.accessoryType = UITableViewCellEditingStyleNone;
        }
    }
    else
    {
        isCell = YES;

    }
     */

    //if (isCell) {
        
    /*
    NSManagedObject* item = [self.arrNotilist objectAtIndex:indexPath.row];
    NSDate *createDate = [item valueForKey:@"createDate"];
    cell.detailTextLabel.text = [self _getCurrentStringTime:createDate];
    cell.textLabel.text = [item valueForKey:@"url"];
    cell.textLabel.numberOfLines = 2;
        
    //액세서리 뷰 이미지 바꾸기~ 
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow" ofType:@"png"]];
    
    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
    
    cell.accessoryView = imgView;
    
    [imgView release];
    //}
    
    item = nil;
    imgView = nil;
    image = nil;
         
    return cell;
    */
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

    /*
        
    BOOL isGoNotiContentView = NO;
    
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if ((nPageNum * kPageDivide) < [arrNotilist count]) {
        
        if(indexPath.row != (nPageNum * kPageDivide))
        {
            
            isGoNotiContentView = YES;
            
        }
        else
        {
            nPageNum++;
            
            activityIndicator = [[UIActivityIndicatorView alloc] initWithFrame:CGRectMake(0, 0, 32, 32)];
            [activityIndicator setCenter:CGPointMake(160, 208)];
            [activityIndicator setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleGray];
            [self.view addSubview:activityIndicator];
            [activityIndicator release];
            [activityIndicator startAnimating];
            //2초 후에 정지하기
            [self performSelector:@selector(stopAnimation) withObject:nil afterDelay:0.5];
            
            
            [self.tableView reloadData];
        }
    }
    else
    {
        isGoNotiContentView = YES;
    }
     */
   
    
    //if (isGoNotiContentView) {
    
    NSManagedObject* item = [self.arrNotilist objectAtIndex:indexPath.row];

    AkBarcodeHistoryDetailView *barcodeHistoryDetail = [[AkBarcodeHistoryDetailView alloc] init] ;
        
    barcodeHistoryDetail.strURL = [item valueForKey:@"url"];

    
    [self.navigationController pushViewController:barcodeHistoryDetail animated:YES];
    //}

    item = nil;
    
}







#pragma activityIndicator
-(void)stopAnimation
{
    [activityIndicator stopAnimating];
}

/*
#pragma mark -
#pragma mark Data Source Loading / Reloading Methods

- (void)reloadTableViewDataSource{
	
   
	//  should be calling your tableviews data source model to reload
	//  put here just for demo
	_reloading = YES;
    
    nPageNum++;
    
	[self.tableView reloadData];
    
    
//
    int nTotalCnt = [arrNotilist count];
    
    if ((nPageNum * kPageDivide) < nTotalCnt)
    {
        _refreshHeaderView.frame = CGRectMake(0.0f, self.tableView.bounds.size.height + (50 * kPageDivide), self.view.frame.size.width, 50);
    }
    else
    {
        
        _refreshHeaderView.frame = CGRectMake(0.0f, self.tableView.bounds.size.height + (20 * (nTotalCnt - kPageDivide)), self.view.frame.size.width, 50);
    }
        
 //   
    
}

*/


@end
