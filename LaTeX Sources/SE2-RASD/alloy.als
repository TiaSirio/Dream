//Signatures
sig Name {}

sig Surname {}

sig Email {}

sig Date {}

sig AreaOfResidence {}

sig PolicyMakerId {}

sig User {
	name: one Name,
	surname: one Surname,
	email: one Email,
	dateOfBirth: one Date,
	areaOfResidence: one AreaOfResidence,
}


sig PolicyMaker extends User {
	policyMakerId: one PolicyMakerId,
}

sig Administrator {
	email: one Email,
	password: one Password,
}

sig Password {}
{ //Each password is associated to an Administrator
	all p : Password | (some a: Administrator | a. password = p)
}

sig Text {}

sig DateTime in Int {}

abstract sig Status {}

sig PENDING extends Status{}

sig ACCEPTED extends Status {}

sig REJECTED extends Status {}

sig Attachment {}

sig DiscussionTitle {}

sig SubTitle {}

sig TopicTitle {}

abstract sig Visibility {}

sig Visible extends Visibility {}

sig Invisible extends Visibility {}

sig Topic {
	title: one TopicTitle,
	timestamp: one DateTime,
	discussions: some Discussion,
}

sig Discussion {
	title: one DiscussionTitle,
	subTitle: one SubTitle,
	timestamp: one DateTime,
	creator: one PolicyMaker,
	posts: some Post,
}

sig Post {
	text: one Text,
	creator: one User,
	timestamp: one DateTime,
	attachment: lone Attachment,
	status: one Status,
	visibility: one Visibility,
}

sig DataName {}

sig Source {}

sig Description {}

sig DataType {
	name: one DataName,
}

sig DataSource {
	name: one DataName,
	source: one Source,
	description: lone Description,
	dataType: one DataType,
}

-----------------------------------------------------------------------------------------------------------------
//Facts

fact { //A name exist only if it's present an User
	all n: Name | one u: User | u.name = n
}

fact { //A surname exist only if it's present an User
	all s: Surname | one u: User | u.surname = s
}

fact { //Each policyMakerId is unique
	no disj p1, p2: PolicyMaker | p1.policyMakerId = p2.policyMakerId
}

fact { //Each User has an unique email
	no disj u1, u2 : User | u1.email = u2.email
}

fact { //Each Administrator has an unique email
	no disj a1, a2 : Administrator | a1.email = a2.email
}

fact { //Administrators and Users  have different emails
	no disj a: Administrator, u: User | a.email = u.email
}

fact { //If a post exist, it must be PENDING, ACCEPTED or REJECTED
	all p: Post | p.status = PENDING or p.status = ACCEPTED or p.status = REJECTED
}

fact { //Two Users can not be creators of the same Post
	all p: Post | (no disj u1, u2: User | (u1 = p.creator and u2 = p.creator and u1 != u2))
}

fact { //Two Policy makers can not be creators of the same Discussion
	all d: Discussion | (no disj p1, p2: PolicyMaker | (p1 = d.creator and p2 = d.creator and p1 != p2))
}

fact { //A Post always belong to one Discussion
	all p: Post | one d: Discussion | p in d.posts
}

fact { //A Discussion always belong to one Topic
	all d: Discussion | one t: Topic | d in t.discussions
}

fact { //A Discussion has always a title
	all d: Discussion | one t: DiscussionTitle | d.title = t
}

fact { //A Topic has always a title
	all to: Topic | one ti: TopicTitle | to.title = ti
}

fact { //A Post can not exists without a creator
	all p: Post | one u: User | u = p.creator
}

fact { //A Discussion can be created only by a Policy maker
	all d: Discussion | one p: PolicyMaker | p = d.creator and p.policyMakerId != none
}

fact { //There can not exist a Post without Text and without Attachment
	no disj p: Post | p.text = none and p.attachment = none
}

fact { //A Post is not visible if it has been rejected or is still in the pending list
	all p: Post | (p.status = PENDING or p.status = REJECTED) implies p.visibility = Invisible
}

fact { //A Post is visible if it has been accepted
	all p: Post | p.status = ACCEPTED implies p.visibility = Visible
}

fact { //An attachment exist only if it's present a Post
	all a: Attachment | one p: Post | p.attachment = a
}

fact { //A text exist only if it's present a Post
	all t: Text | one p: Post | p.text = t
}

fact { //A subTitle exist only if it's present a Discussion
	all s: SubTitle | one d: Discussion | d.subTitle = s
}

fact { //A discussion has at least one ACCEPTED post
	all d: Discussion | some p: Post | p.status = ACCEPTED and p in d.posts
}

fact { //Each post is unique
	no disj p1, p2: Post | p1 = p2
}

fact { //Each discussion is unique
	no disj d1, d2: Discussion | d1 = d2
}

fact { //Each topic is unique
	no disj t1, t2: Topic | t1 = t2
}

fact { //A post of a Policy maker is always accepted
	no p: Post | one pm: PolicyMaker | pm = p.creator and p.status != ACCEPTED
}

fact { //Each topic has an unique title
	no disj t1, t2 : Topic | t1.title = t2.title
}

fact { //Each discussion has an unique title
	no disj d1, d2 : Discussion | d1.title = d2.title
}

fact { //A description exist only if it's present a DataSource
	all d: Description | one ds: DataSource | ds.description = d
}

fact { //A source exist only if it's present a DataSource
	all s: Source | one ds: DataSource | ds.source = s
}

fact { //A Data source can not exist without a source
	all d: DataSource | one s: Source | d.source = s
}

fact { //DataSource and DataType have different names
	no disj ds: DataSource, dt: DataType | ds.name = dt.name
}

fact { //Two DataType have different names
	no disj dt1, dt2: DataType | dt1.name = dt2.name
}



-----------------------------------------------------------------------------------------------------------------
//Assertions

//G2: Allow the platform Administrator to decide which public data should be used in the Deviance computation
assert dataSourceIsInserted {
	all ds: DataSource | one dt: DataType, s: Source | ds.source = s and ds.dataType = dt
}
check dataSourceIsInserted for 40

// G3: Allow people to interact and build a knowledge network
assert publishAPost {
	no p: Post | all d: Discussion | some u: User | u = p.creator and p not in d.posts
}
check publishAPost for 40

// G3: Allow people to interact and build a knowledge network
assert confirmAPost {
	no p: Post | one d: Discussion | p.status = ACCEPTED and p.visibility = Invisible and p in d.posts
}
check confirmAPost for 40

// G5: Allow Policy Makers to release publicly their reports based on the Deviance result
assert createADiscussion {
	all d: Discussion | some p: Post | one pm: PolicyMaker, t: Topic | p in d.posts and d.creator = pm and d in t.discussions and p.status = ACCEPTED
}
check createADiscussion for 20

-----------------------------------------------------------------------------------------------------------------
//Predicates


pred dataAdministration {	
	# Administrator > 0
	# DataSource > 3
	# Topic = 0
	# Discussion = 0
	# Post = 0
	# User = 0
}
run dataAdministration for 10


pred forum {
	# Topic > 2
	# Discussion > 3
	# Post > 3
	# DataSource = 0
	# DataType = 0
}
run forum for 10

pred accounts {
	# Administrator > 2
	# PolicyMaker > 2
	# User > 4
	# DataSource = 0
	# DataType = 0
	# Topic = 0
}
run accounts for 10


































