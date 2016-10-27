#define kroot_Item      @"root"

//onlyak//////////////////////////////////////////
#define kNotiList_item      [[[NSArray alloc] initWithObjects:@"alarm",  nil] autorelease]
#define kNotiList_items       @"item"
#define kNotiListElements               [[[NSDictionary alloc] initWithObjectsAndKeys:[[[NSArray alloc] initWithObjects:@"PUSH_ID", @"EVENT_NAME", @"TITLE", @"SHORT_URL",  @"SEND_DT", @"FACEBOOK", @"TWITTER", @"READ", nil] autorelease], @"alarm", nil] autorelease]



#define NOTILIST_DETAIL_ATTREBUTE [[[NSArray alloc] initWithObjects:kroot_Item, kNotiList_item, kNotiList_items, kNotiListElements, nil] autorelease]