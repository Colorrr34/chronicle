export type User = {
  id: string;
  username: string;
  createdAt?: string;
  updatedAt?: string;
  LastLoggedInAt?: string;
};

export type Topic = {
  id: string;
  topic: string;
};

export type Feed = {
  id: string;
  title: string;
  topic: string;
  createdAt?: string;
  updatedAt?: string;
};

export type Post = {
  id: string;
  title: string;
  description: string;
  url: string;
  feed: Feed;
  createdAt?: string;
  updatedAt?: string;
  publishedAt?: string;
};
