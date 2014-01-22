package pl.project13.scala.akka.raft.protocol

import akka.actor.ActorRef
import pl.project13.scala.akka.raft.Term

trait InternalProtocol {

  // todo maybe throw out
  // just some types to make it more clear when these messages are sent, not actualy used (could be stripped)
  sealed trait InternalMessage  extends Message[Internal]
  sealed trait FollowerResponse extends Message[Internal]
  sealed trait ElectionMessage  extends Message[Internal]
  sealed trait LeaderMessage    extends Message[Internal]

  case object BeginElection     extends ElectionMessage
  case class Vote(term: Term)   extends ElectionMessage
  case class Reject(term: Term) extends ElectionMessage // todo needs better name

  case class ElectedAsLeader()   extends ElectionMessage
  case object ElectionTimeout   extends ElectionMessage

  /** When the Leader has sent an append, for an unexpected number, the Follower replies with this */
  sealed trait AppendResponse {
    /** currentTerm for leader to update in the `nextTerm` lookup table */
    def term: Term
  }
  case class AppendRejected(term: Term)    extends FollowerResponse
  case class AppendSuccessful(term: Term)  extends FollowerResponse

  case object SendHeartbeat extends LeaderMessage

  // internal cluster related messages
  case class MembersChanged(members: Vector[ActorRef])
}