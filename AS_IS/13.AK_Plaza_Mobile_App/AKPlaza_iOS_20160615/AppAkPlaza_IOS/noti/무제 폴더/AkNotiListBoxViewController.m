//
//  AkNotiListBoxViewController.m
//  AppAkPlaza_iOS
//
//  Created by uniwis on 11. 6. 13..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "AkNotiListBoxViewController.h"
#import "AkNotiContentViewController.h"
#import "UWTabBar.h"
#define kPageDivide  10

@implementation AkNotiListBoxViewController

 
@synthesize tableView,arrNotilist, nPageNum, activityView, oModel,pushid;


- (void)dealloc
{

    [tableView release];
    [arrNotilist release];
    [activityView release];
    
    [oModel release];
    [pushid release];
    
    [super dealloc];
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
    self.view = [[[NSBundle mainBundle] loadNibNamed:@"UWNavigation" owner:self options:NULL] lastObject];
    
    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"btn_top_pre" ofType:@"png"]] forState:UIControlStateNormal];
    [self.btnLeft setImage:[UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"btn_top_pre_s" ofType:@"png"]] forState:UIControlStateHighlighted];

    
    //Set the title
	[self viewLblNaviTitle:@"알림보관함"];
    

}


- (void)activityStartView:(UIActivityIndicatorView *)act
{
    //로딩 화면 출력될 때 ActivityIndicatorView 보이기
	self.activityView = act;
    
	self.activityView.center = CGPointMake(160, 169);
	
    [self.contentView insertSubview:self.activityView atIndex:999];
    
    [self.activityView startAnimating];
}


-(void)viewDidLoad
{
    [super viewDidLoad];
    
    nPageNum = 1;
    
    self.oModel = [[AkNotiModel alloc] init];
    
    self.oModel.delegate = self;
    

        
    CGRect tableViewFrame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y, self.contentView.frame.size.width, self.contentView.frame.size.height);;
    self.tableView = [[UITableView alloc ] initWithFrame:tableViewFrame style:UITableViewStylePlain];

   self.tableView.dataSource = self;
    self.tableView.delegate = self;
    [self.tableView setBackgroundColor:[UIColor clearColor]];
    [self.tableView setSeparatorStyle:UITableViewCellSeparatorStyleNone];		 
    //[view release];
    
    [self.contentView addSubview:self.tableView];
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    self.tableView = nil;
    self.arrNotilist = nil;
    self.activityView = nil;
    self.oModel = nil;
    self.pushid = nil;
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    

    
    
    self.arrNotilist = [[[NSMutableArray alloc] initWithArray:[self.oModel performSelector:@selector(notiList)]] autorelease];
    
    [self.oModel activityStop];
    
    [self.tableView reloadData];
    
    

    if (self.pushid && [self.pushid length] > 0) {
        for (NSMutableDictionary* dicParam in self.arrNotilist) {
            NSString* strPushID = [NSString stringWithString: [dicParam objectForKey:@"PUSH_ID"]];
            
            if ([self.pushid isEqualToString:strPushID]) {
                
                AkNotiContentViewController *akNoti = [[[AkNotiContentViewController alloc] init] autorelease];
                
                akNoti.arrNotiContent = dicParam;
                
                [self.navigationController pushViewController:akNoti animated:YES];
                
                self.pushid = nil;
                
                return;
            }
            
            
        }
    }

        
    
    
    
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
//// Section header & footer information. Views are preferred over title should you decide to provide both
//
//- (UIView *)tableView:(UITableView *)tView viewForFooterInSection:(NSInteger)section
//{
//
//    return self.refreshHeaderView;
//}



- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    //#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    //#warning Incomplete method implementation.
    // Return the number of rows in the section.
    
    return [self.arrNotilist count];
     
    
    //return [arrNotilist count];
    
}



- (UITableViewCell *)tableView:(UITableView *)tView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier] autorelease];
    }
     
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
    
    
    
    NSMutableDictionary *items = [[[NSMutableDictionary alloc] initWithDictionary:[self.arrNotilist objectAtIndex:indexPath.row]] autorelease];
    
    cell.textLabel.text = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:2]];

    

        
    //NSDictionary* item = [self.arrNotilist objectAtIndex:indexPath.row];
    cell.detailTextLabel.text = [items objectForKey:[[kNotiListElements objectForKey:@"alarm"] objectAtIndex:3]];

    //cell.textLabel.text = [item objectForKey:@"NotiName"];
    
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    //액세서리 뷰 이미지 바꾸기~ 
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"arrow_black_right" ofType:@"png"]];
    
    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
    
    cell.accessoryView = imgView;
    
    [imgView release];
    
    //}
         
    return cell;
}




-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    UIImage *image = nil;
    UIImage *image_s = nil;
    
    //cell backgroundView로 바꿔주기 
    if ((indexPath.row % 2) == 0)
	{
        image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"cate_listbg_a" ofType:@"png"]];
        image_s = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"cate_listbg_a_s" ofType:@"png"]];
        
	}
	else
	{
        image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"cate_listbg_b" ofType:@"png"]];
        image_s = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"cate_listbg_b_s" ofType:@"png"]];
        
	}
    
    UIImageView* imgView = [[UIImageView alloc] initWithImage:image];
    UIImageView* imgView_s = [[UIImageView alloc] initWithImage:image_s];
    
    cell.backgroundView = imgView;
    cell.selectedBackgroundView = imgView_s;
    
    [imgView release];
    [imgView_s release];
    
    //selected 됬을때... 

    cell.textLabel.highlightedTextColor  = [UIColor blackColor];
    cell.detailTextLabel.highlightedTextColor  = [UIColor blackColor];
    cell.textLabel.font = [UIFont systemFontOfSize:15.0];
    cell.detailTextLabel.font = [UIFont systemFontOfSize:12.0];
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
        AkNotiContentViewController *akNoti = [[[AkNotiContentViewController alloc] init] autorelease];
   
        akNoti.arrNotiContent = [self.arrNotilist objectAtIndex:indexPath.row];

        [self.navigationController pushViewController:akNoti animated:YES];
    //}

       
    
}







#pragma activityIndicator

-(void)doLeftButton:(id)sender
{
    
    for (UIView *view in [self.contentView subviews]) {
        [view removeFromSuperview];
    }
    
    [self.navigationController popViewControllerAnimated:YES];
}

@end
