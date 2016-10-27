//
//  AkNotiListBoxViewController.h
//  AppAkPlaza_iOS
//
//  Created by uniwis on 11. 6. 13..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "AkBarcodeHistoryDetailView.h"
#import "fontSizeUtil.h"

@interface AkBarcodeHistoryListView : UWNavigation <UITableViewDelegate, UITableViewDataSource, UITabBarControllerDelegate> {
    
    UITableView *tableView;
    
    NSArray					*arrNotilist;
    
    int nPageNum;
    
    UIActivityIndicatorView *activityIndicator;
    
    
    
    
    //  Reloading var should really be your tableviews datasource
	//  Putting it here for demo purposes 
	BOOL _reloading;
    
} 

@property (nonatomic, retain) UITableView *tableView;
@property (nonatomic, retain) NSArray *arrNotilist;
@property int nPageNum;




@property (nonatomic, retain) UIActivityIndicatorView *activityIndicator;

- (void)stopAnimation;

- (NSString *)_getCurrentStringTime:(NSDate*)createDate;
- (UITableViewCell *)tableViewCellWithReuseIdentifier:(NSString *)identifier;
- (void)configureCell:(UITableViewCell *)cell forIndexPath:(NSIndexPath *)indexPath;


@end
