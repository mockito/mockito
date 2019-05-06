> Hey, 
> 
> Thanks for the contribution, this is awesome.
> As you may have read, project members have somehow an opinionated view on what and how should be
> Mockito, e.g. we don't want mockito to be a feature bloat.
> There may be a thorough review, with feedback -> code change loop.
> 
> Which branch : 
> - On mockito 2.x, make your pull request target `release/2.x`
> - On next mockito version make your pull request target `release/3.x`
>
> _This block can be removed_
> _Something wrong in the template fix it here `.github/PULL_REQUEST_TEMPLATE.md`


check list

 - [ ] Read the [contributing guide](https://github.com/mockito/mockito/blob/release/2.x/.github/CONTRIBUTING.md)
 - [ ] PR should be motivated, i.e. what does it fix, why, and if relevant how
 - [ ] If possible / relevant include an example in the description, that could help all readers
       including project members to get a better picture of the change
 - [ ] Avoid other runtime dependencies
 - [ ] Meaningful commit history ; intention is important please rebase your commit history so that each
       commit is meaningful and help the people that will explore a change in 2 years
 - [ ] The pull request follows coding style
 - [ ] Mention `Fixes #<issue number>` in the description _if relevant_
 - [ ] At least one commit should mention `Fixes #<issue number>` _if relevant_

